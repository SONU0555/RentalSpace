package rentalSpacePortfolio.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.payment.VerifyPaymentRequest;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.dto.response.payment.OrderResponse;
import rentalSpacePortfolio.entity.Booking;
import rentalSpacePortfolio.entity.Payment;
import rentalSpacePortfolio.enums.BookingStatus;
import rentalSpacePortfolio.enums.BookingType;
import rentalSpacePortfolio.enums.PaymentCategory;
import rentalSpacePortfolio.enums.PaymentStatus;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.BookingResponseMapper;
import rentalSpacePortfolio.repository.PaymentRepository;
import rentalSpacePortfolio.service.interfaces.PaymentGateway;
import rentalSpacePortfolio.repository.BookingRepository;

@Slf4j
@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepo;
    private final BookingRepository flatBookingRepo;
    private final BookingRepository bookingRepo;
    private final PaymentGateway paymentGateway;
    private final FakePaymentGateway fakePaymentGateway;
   
    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
            BookingRepository flatBookingRepo,
            BookingRepository bookingRepo,
            PaymentGateway paymentGateway,
            FakePaymentGateway fakePaymentGateway){
        this.paymentRepo = paymentRepository;
        this.flatBookingRepo = flatBookingRepo;
        this.bookingRepo = bookingRepo;
        this.paymentGateway = paymentGateway;
        this.fakePaymentGateway = fakePaymentGateway;
    }
    
    @Transactional
    public OrderResponse createOrder(UUID bookingId, String paymentMode, String idempotencyKey){
        
        Optional<Payment> existing = paymentRepo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
        // Same key seen before — don't create a duplicate, return the original result
        Payment payment = existing.get();
        String existBookingId = String.valueOf(payment.getBooking().getId());
        return new OrderResponse(payment.getOrderId(), existBookingId, payment.getAmount(), "INR");
        }
        
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            log.warn("Payment failed: Booking is not in a payable state to book it.");
            throw new IllegalStateException("Booking is not in a payable state");
        }
        if (booking.getHoldExpireAt() != null && booking.getHoldExpireAt().isBefore(LocalDateTime.now())) {
            log.warn("Payment Failed: Your booking has expired, please book again and try");
            throw new IllegalStateException("Booking hold has expired, please book again");
        }
        
        OrderResponse order = paymentGateway.createOrder(String.valueOf(bookingId), booking.getTotalAmount());
        
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setOrderId(order.getOrderId());
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCategory(booking.getBookingType() == BookingType.FLAT
                ? PaymentCategory.FLAT_BOOKING
                : PaymentCategory.AMENITY_BOOKING);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setStatus(PaymentStatus.INITIATED);

        paymentRepo.save(payment);
        
        fakePaymentGateway.verifyPayment(order.getOrderId(), "txn_" + UUID.randomUUID());
        
        return order;
        
    }
    
    
    @Transactional
    public FlatBookingResponse verifyAndFinalize(VerifyPaymentRequest req){
        log.info("Verify and finalize booking payment");
        Payment payment = paymentRepo.findByOrderId(req.getOrderId())
                .orElseThrow(() ->{
                    log.warn("Verification failed: Payment not found with order ID: {}", req.getOrderId());
                    return new ResourceNotFoundException("Payment not found");
                });
        
        payment.setTransactionId(req.getTransactionId());
        payment.setStatus(PaymentStatus.valueOf(req.getSimulatedStatus()));
        paymentRepo.save(payment);
        
        Booking booking = bookingRepo.findById(payment.getBooking().getId())
                .orElseThrow(() -> {
                    log.warn("verification failed: Booking not found with ID: {}", payment.getBooking().getId());
                    return new ResourceNotFoundException("Booking not found");
                });
        
        if(req.getSimulatedStatus().equals("SUCCESS")){
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setIsPaid(Boolean.TRUE);
        }else{
            booking.setStatus(BookingStatus.FAILED);
        }
        
        Booking updatedBooking = bookingRepo.save(booking);
        
        return BookingResponseMapper.mapToBookingHistoryResponseDto(updatedBooking);
    }


}