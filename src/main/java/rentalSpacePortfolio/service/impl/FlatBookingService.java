package rentalSpacePortfolio.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.flat.FlatBookingRequest;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.FlatBooking;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.enums.FlatBookingStatus;
import rentalSpacePortfolio.exception.BadRequestException;
import rentalSpacePortfolio.exception.BookingConflictException;
import rentalSpacePortfolio.exception.FlatNotAvailableException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.FlatResponseMapper;
import rentalSpacePortfolio.repository.FlatBookingRepository;
import rentalSpacePortfolio.repository.FlatRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.TenantRepository;

@Slf4j
@Service
public class FlatBookingService {
    
    private final FlatBookingRepository flatBookingRepo;
    private final TenantRepository tenantRepo;
    private final FlatRepository flatRepo;
    private final PropertyRepository propertyRepo;
    
    @Autowired
    public FlatBookingService(
            FlatBookingRepository flatBookingRepo,
            TenantRepository tenantRepo,
            FlatRepository flatRepo,
            PropertyRepository propertyRepo){
        this.flatBookingRepo = flatBookingRepo;
        this.tenantRepo = tenantRepo;
        this.flatRepo = flatRepo;
        this.propertyRepo = propertyRepo;
    }
    
    @Transactional
    public FlatBookingResponse createBooking(FlatBookingRequest req){
        
        log.info("Received request to create flat booking");
        
        Tenant tenant = tenantRepo.findById(req.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        
        if(tenant.getUser().getPhone() == null || tenant.getEmergencyContect() == null){
            log.warn("User: {} action not allowed due to incomplete profile", tenant.getId());
            throw new BadRequestException("Flat Booking Failed: can't process booking with incomplete profile");
        }
        
        if(!tenant.getIsVerified()){
            log.warn("User: {} action not allowed, AADHAAR-CARD verification required", tenant.getId());
            throw new BadRequestException("Flat Booking Failed: can't process booking, AADHAAR-CARD verification required");
        }
        
        Flat flat;
            try {
                // This line will wait maximum 3 seconds for one user to finish
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

        // Availability checking
        log.info("Checking availability, is flat already booked or not");
        List<FlatBooking> overlaps = flatBookingRepo.findOverlappingBookings(
            flat.getId(), req.getLeaseStartDate(), leaseEnd,
            List.of(FlatBookingStatus.PENDING, FlatBookingStatus.CONFIRMED));

        if (!overlaps.isEmpty()) {
            log.warn("Flat with ID: {} already booked/held for these dates", flat.getId());
            throw new FlatNotAvailableException("Flat already booked or held for these dates");
        }

        Double total = flat.getRentAmount() + flat.getSecurityDeposit(); // monthly rent + security deposit
        
        FlatBooking booking = new FlatBooking();
        booking.setTenant(tenant);
        booking.setFlat(flat);
        booking.setProperty(property);
        booking.setLeaseStartDate(req.getLeaseStartDate());
        booking.setLeaseEndDate(leaseEnd);
        booking.setLeaseDurationMonths(req.getLeaseDurationMonths());
        booking.setMonthlyRent(flat.getRentAmount());
        booking.setSecurityDeposit(flat.getSecurityDeposit());
        booking.setTotalAmount(total);
        booking.setStatus(FlatBookingStatus.PENDING);
        booking.setIsPaid(false);

        FlatBooking saved = flatBookingRepo.save(booking);
        
        log.info("Booking created successfully and saved to DB");
        return FlatResponseMapper.mapToFlatBookingDto(saved);
    }
    
    // Service to get booking by ID
    public FlatBookingResponse getBookingById(UUID bookingId){
        log.info("Received request to find booking by ID");
        FlatBooking booking = flatBookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking now found with ID: " + bookingId));
        
        return FlatResponseMapper.mapToFlatBookingDto(booking);
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
        List<FlatBooking> tenantBookingFlats = flatBookingRepo.findByTenantId(tenantId);
        return tenantBookingFlats.stream().map(b 
                -> FlatResponseMapper.mapToBookingHistoryResponseDto(b)).collect(Collectors.toList());
    }
    
    // Service to cancel booking
    @Transactional
    public void cancelBooking(UUID bookingId, String reason, String cancelledBy) {
        log.info("Received request to cancel booking ID: {} by {}", bookingId, cancelledBy);
        FlatBooking booking = flatBookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == FlatBookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }

        booking.setStatus(FlatBookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelledBy(cancelledBy);

        flatBookingRepo.save(booking);
    }
    
    // Mark created booking as confirmed after successfull payment
    @Transactional
    public void markBookingConfirmed(UUID bookingId, String paymentId) {
        log.info("Received payment success respond");
        FlatBooking booking = flatBookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(FlatBookingStatus.CONFIRMED);
        booking.setIsPaid(true);
        booking.setPaymentId(paymentId);
        flatBookingRepo.save(booking);
    }

}