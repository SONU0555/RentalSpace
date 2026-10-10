package rentalSpacePortfolio.service.impl;

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
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.BookingStatus;
import rentalSpacePortfolio.enums.BookingType;
import rentalSpacePortfolio.enums.FlatStatus;
import rentalSpacePortfolio.exception.BadRequestException;
import rentalSpacePortfolio.exception.BookingConflictException;
import rentalSpacePortfolio.exception.FlatNotAvailableException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.FlatResponseMapper;
import rentalSpacePortfolio.repository.FlatRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.BookingRepository;
import rentalSpacePortfolio.repository.FlatBookingRepository;
import rentalSpacePortfolio.repository.PaymentRepository;
import rentalSpacePortfolio.repository.UserRepository;

@Slf4j
@Service
public class FlatBookingService {
    
    private final BookingRepository bookingRepo;
    private final FlatBookingRepository flatBookingRepo;
    private final PaymentRepository paymentRepo;
    private final TenantRepository tenantRepo;
    private final FlatRepository flatRepo;
    private final UserRepository userRepo;
    
    @Autowired
    public FlatBookingService(
            BookingRepository bookingRepo,
            FlatBookingRepository flatBookingRepo,
            PaymentRepository paymentRepo,
            TenantRepository tenantRepo,
            FlatRepository flatRepo,
            UserRepository userRepo
    ){
        this.bookingRepo = bookingRepo;
        this.flatBookingRepo = flatBookingRepo;
        this.paymentRepo = paymentRepo;
        this.tenantRepo = tenantRepo;
        this.flatRepo = flatRepo;
        this.userRepo = userRepo;
    }
    
    @Transactional
    public FlatBookingResponse createFlatBooking(UUID userId, UUID flatId, FlatBookingRequest req){
        
        log.info("Received request to create flat booking");
        
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Tenant tenant = user.getTenant();
        
        // Verifying is the user has complete profile or not
        if(user.getPhone() == null || tenant.getEmergencyContect() == null){
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
                flat = flatRepo.findByIdForUpdate(flatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Flat not found"));
            
            } catch (PessimisticLockingFailureException ex) {
                // Another User falls here if first User takes too long
                log.warn("Lock acquisition timed out for flat ID: {}. Another booking is in progress.", flatId);
                throw new BookingConflictException("The system is currently processing a booking for this flat. Please try again in a few seconds.");
        }
        
        LocalDate leaseEndDate = req.getLeaseStartDate().plusMonths(req.getLeaseDurationMonths());

        // Flat availability checking
        log.info("Checking availability, is flat already booked or not");
        List<FlatBooking> overlaps = flatBookingRepo.findOverlappingBookings(flat.getId(), req.getLeaseStartDate(), leaseEndDate,
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

        FlatBooking flatBooking = new FlatBooking();
        flatBooking.setFlat(flat);
        flatBooking.setLeaseStartDate(req.getLeaseStartDate());
        flatBooking.setLeaseEndDate(leaseEndDate);
        flatBooking.setLeaseDurationMonths(req.getLeaseDurationMonths());
        flatBooking.setMonthlyRent(flat.getRentAmount());
        flatBooking.setSecurityDeposit(flat.getSecurityDeposit());

        // link both sides
        booking.setFlatBooking(flatBooking);
        flatBooking.setBooking(booking);

        booking = bookingRepo.save(booking);   // cascade = ALL also saves flatBooking
        
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
        
        log.info("Tenant is valid");
        // fetch flat bookings and map to response dto
        List<Booking> tenantBookingFlats = bookingRepo.findFlatByTenantId(tenantId);
        if(tenantBookingFlats.isEmpty()){
            return null;
        }
        
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
        
        Flat bookedFlat = flatRepo.findById(booking.getFlatBooking().getFlat().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found"));

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelledBy(cancelledBy);
        bookedFlat.setStatus(FlatStatus.VACANT);

        bookingRepo.save(booking);
        flatRepo.save(bookedFlat);
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
    
    @Transactional
    public void markCheckIn(UUID bookingID, LocalDate date){
        log.info("Received request to mark booking: {} check in", bookingID);
        Booking booking = bookingRepo.findById(bookingID)
                .orElseThrow(() -> {
                    log.warn("Mark booking checkIn Failed: booking not found with Id: {}", bookingID);
                    return new ResourceNotFoundException("Booking not found");
                });
        
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            log.warn("Mark booking checkIn Failed: Booking {} is in {} status", bookingID, booking.getStatus());
            throw new IllegalStateException("Cannot check-in. The booking must be CONFIRMED.");
        }

        FlatBooking bookedFlat = booking.getFlatBooking();
        bookedFlat.setCheckInDate(date);
        
        flatBookingRepo.save(bookedFlat);
        
        log.info("Successfully marked booking: {} as checked in at {}", bookingID, date);
    }
    
    @Transactional
    public void markCheckOut(UUID bookingID, LocalDate date){
        log.info("Received request to mark booking: {} check out", bookingID);
        Booking booking = bookingRepo.findById(bookingID)
                .orElseThrow(() -> {
                    log.warn("Mark booking checkOut Failed: booking not found with Id: {}", bookingID);
                    return new ResourceNotFoundException("Booking not found");
                });
        
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            log.warn("Mark booking checkOut Failed: Booking {} is in {} status", bookingID, booking.getStatus());
            throw new IllegalStateException("Cannot check-Out. The booking must be CONFIRMED.");
        }

        FlatBooking bookedFlat = booking.getFlatBooking();
        bookedFlat.setCheckOutDate(date);
        
        flatBookingRepo.save(bookedFlat);
        
        log.info("Successfully marked booking: {} as checked out at {}", bookingID, date);
    }
    
    @Transactional
    public void deleteBooking(UUID bookingId){
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Booking not found with Id: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found");
                });
        
        Flat bookedFlat = booking.getFlatBooking().getFlat();
        if(bookedFlat.getStatus() == FlatStatus.OCCUPIED){
            bookedFlat.setStatus(FlatStatus.VACANT);
            flatRepo.save(bookedFlat);
        }
        
        bookingRepo.delete(booking);
    }

}