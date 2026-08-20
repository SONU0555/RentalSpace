package rentalSpacePortfolio.service.impl;

import jakarta.persistence.criteria.LocalDateTimeField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.flat.FlatBookingRequest;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.Booking;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.FlatBooking;
import rentalSpacePortfolio.entity.Payment;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.enums.BookingStatus;
import rentalSpacePortfolio.enums.BookingType;
import rentalSpacePortfolio.exception.BadRequestException;
import rentalSpacePortfolio.exception.BookingConflictException;
import rentalSpacePortfolio.exception.FlatNotAvailableException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.FlatResponseMapper;
import rentalSpacePortfolio.repository.FlatRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.BookingRepository;
import rentalSpacePortfolio.repository.FlatBookingRepository;
import rentalSpacePortfolio.repository.PaymentRepository;

@Slf4j
@Service
public class BookingService {
    
    private final BookingRepository bookingRepo;
    private final FlatBookingRepository flatBookingRepo;
    private final PaymentRepository paymentRepo;
    private final TenantRepository tenantRepo;
    private final FlatRepository flatRepo;
    private final PropertyRepository propertyRepo;
    
    @Autowired
    public BookingService(
            BookingRepository bookingRepo,
            FlatBookingRepository flatBookingRepo,
            PaymentRepository paymentRepo,
            TenantRepository tenantRepo,
            FlatRepository flatRepo,
            PropertyRepository propertyRepo){
        this.bookingRepo = bookingRepo;
        this.flatBookingRepo = flatBookingRepo;
        this.paymentRepo = paymentRepo;
        this.tenantRepo = tenantRepo;
        this.flatRepo = flatRepo;
        this.propertyRepo = propertyRepo;
    }
    
    @Transactional
    public FlatBookingResponse createFlatBooking(FlatBookingRequest req){
        
        log.info("Received request to create flat booking");
        
        Tenant tenant = tenantRepo.findById(req.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        
        // Verifying is the user has complete profile or not
        if(tenant.getUser().getPhone() == null || tenant.getEmergencyContect() == null){
            log.warn("User: {} action not allowed due to incomplete profile", tenant.getId());
            throw new BadRequestException("Flat Booking Failed: can't process booking with incomplete profile");
        }
        
        // Checking is the user verified or not with aadhaar
        if(!tenant.getIsVerified()){
            log.warn("User: {} action not allowed, AADHAAR-CARD verification required", tenant.getId());
            throw new BadRequestException("Flat Booking Failed: can't process booking, AADHAAR-CARD verification required");
        }
        
        Flat flat;
            try {
                // another user will wait maximum 3 seconds for first user to finish
                flat = flatRepo.findByIdForUpdate(req.getFlatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flat not found"));
            
            } catch (PessimisticLockingFailureException ex) {
                // Another User falls here if first User takes too long
                log.warn("Lock acquisition timed out for flat ID: {}. Another booking is in progress.", req.getFlatId());
                throw new BookingConflictException("The system is currently processing a booking for this flat. Please try again in a few seconds.");
        }
        

        Property property = propertyRepo.findById(req.getPropertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        LocalDate leaseEnd = req.getLeaseStartDate().plusMonths(req.getLeaseDurationMonths());

        // Flat availability checking
        log.info("Checking availability, is flat already booked or not");
        List<FlatBooking> overlaps = flatBookingRepo.findOverlappingBookings(flat.getId(), req.getLeaseStartDate(), leaseEnd,
            List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));

        if (!overlaps.isEmpty()) {
            log.warn("Flat with ID: {} already booked/held for these dates", flat.getId());
            throw new FlatNotAvailableException("Flat already booked or held for these dates. Please try again in 5 minutes.");
        }

        Double total = flat.getRentAmount() + flat.getSecurityDeposit(); // monthly rent + security deposit
        
        Booking booking = new Booking();
        booking.setTenant(tenant);
        booking.setTotalAmount(total);
        booking.setStatus(BookingStatus.PENDING);
        booking.setIsPaid(Boolean.FALSE);
        booking.setBookingType(BookingType.FLAT);
        booking.setHoldExpireAt(LocalDateTime.now().plusMinutes(5));
        booking = bookingRepo.save(booking);
        
        FlatBooking flatBooking = new FlatBooking();
//        flatBooking.setTenant(tenant);
        flatBooking.setFlat(flat);
        flatBooking.setProperty(property);
        flatBooking.setLeaseStartDate(req.getLeaseStartDate());
        flatBooking.setLeaseEndDate(leaseEnd);
        flatBooking.setLeaseDurationMonths(req.getLeaseDurationMonths());
        flatBooking.setMonthlyRent(flat.getRentAmount());
        flatBooking.setSecurityDeposit(flat.getSecurityDeposit());
        flatBooking.setBooking(booking);
//        flatBooking.setTotalAmount(total);
//        flatBooking.setStatus(BookingStatus.PENDING);
//        flatBooking.setIsPaid(false);

        flatBooking = flatBookingRepo.save(flatBooking);
        
        log.info("Booking created successfully and saved to DB");
        return FlatResponseMapper.mapToFlatBookingDto(booking, flatBooking);
    }
    
    // Service to get booking by ID
    public FlatBookingResponse getBookingById(UUID bookingId){
        log.info("Received request to find booking by ID");
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking now found with ID: " + bookingId));
        
        return FlatResponseMapper.mapToBookingHistoryResponseDto(booking);
    }
    
    // Service to get booking by Tenant ID
    public List<FlatBookingResponse> getBookingsByTenant(UUID tenantId){
        log.info("Requested to get all bookings done by tenant ID: {}", tenantId);
        tenantRepo.findById(tenantId)
                .orElseThrow(() -> {
                       log.warn("Tenant not found with ID: {}", tenantId);
                       return new ResourceNotFoundException("Tenant not found");
                    }
                );
        
        // fetch flat bookings and map to response dto
        List<Booking> tenantBookingFlats = bookingRepo.findByTenantId(tenantId);
        return tenantBookingFlats.stream().map(b 
                -> FlatResponseMapper.mapToBookingHistoryResponseDto(b)).collect(Collectors.toList());
    }
    
    // Service to cancel booking
    @Transactional
    public void cancelBooking(UUID bookingId, String reason, String cancelledBy) {
        log.info("Received request to cancel booking ID: {} by {}", bookingId, cancelledBy);
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelledBy(cancelledBy);

        bookingRepo.save(booking);
    }
    
    // Mark created booking as confirmed after successfull payment
    @Transactional
    public void markBookingConfirmed(UUID bookingId, UUID paymentId) {
        log.info("Received payment success respond");
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setIsPaid(true);
        booking.getPayments().add(payment);
        bookingRepo.save(booking);
    }

}