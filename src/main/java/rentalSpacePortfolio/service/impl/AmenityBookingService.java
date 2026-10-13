package rentalSpacePortfolio.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.amenity.AmenityBookingRequest;
import rentalSpacePortfolio.dto.response.Amenity.AmenityBookingCreateResponse;
import rentalSpacePortfolio.dto.response.Amenity.AmenityBookingResponse;
import rentalSpacePortfolio.dto.response.Amenity.SlotAvailabilityResponse;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.AmenityBooking;
import rentalSpacePortfolio.entity.Booking;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.enums.BookingStatus;
import rentalSpacePortfolio.enums.BookingType;
import rentalSpacePortfolio.exception.DuplicateBookingException;
import rentalSpacePortfolio.exception.SlotFullException;
import rentalSpacePortfolio.exception.InvalidBookingException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.BookingResponseMapper;
import rentalSpacePortfolio.repository.AmenityBookingRepository;
import rentalSpacePortfolio.repository.AmenityRepository;
import rentalSpacePortfolio.repository.BookingRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;


@Service
@Slf4j
public class AmenityBookingService {
    
    private final AmenityBookingRepository amenityBookingRepo;
    private final AmenityRepository amenityRepo;
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final BookingRepository bookingRepo;
    
    @Autowired
    public AmenityBookingService(
            AmenityBookingRepository amenityBookingRepo,
            AmenityRepository amenityRepo,
            TenantRepository tenantRepo,
            UserRepository userRepo,
            BookingRepository bookingRepo){
        this.amenityBookingRepo = amenityBookingRepo;
        this.amenityRepo = amenityRepo;
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
        this.bookingRepo = bookingRepo;
    }
    
    @Transactional
    public AmenityBookingCreateResponse createAmenityBooking(UUID amenityId, UUID userId, AmenityBookingRequest req){
        log.info("Received request to create amenity booking");
        
        Amenity amenity = amenityRepo.findById(amenityId)
                .orElseThrow(() -> {
                    log.warn("Amenity not found with Id: {}", amenityId);
                    return new ResourceNotFoundException("Amenity not found");
                });
        
        Tenant tenant = tenantRepo.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Tenant not found with Id: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });
                
        if(!bookingRepo.findAmenityByTenantId(tenant.getId(), req.getStartAt()).isEmpty()){
            log.warn("User: {} is trying to book same amenity twice for same date {} time: {} TO {}",
                    tenant.getId(),
                    req.getStartAt().toLocalDate(),
                    req.getStartAt().toLocalTime(),
                    req.getEndAt().toLocalTime());
            
            throw new DuplicateBookingException("You can't book same amenity twice for same date and time.");
        }
        
        long totalDays = ChronoUnit.DAYS.between(req.getStartAt(), req.getEndAt());
        
          if(totalDays > amenity.getAdvanceBookingDays()){
              log.warn("User trying to check amenity for {} days in advance."
                      + " Allowed only {} days in advance", totalDays, amenity.getAdvanceBookingDays());
              throw new InvalidBookingException("You can't book this amenity up to "
                      + totalDays + " days only " + amenity.getAdvanceBookingDays() + " days allowed in advance.");
          }
        
        // This will throw exception if tenant has no assigned/booked flat
        isTenantEligible(tenant);
        
        if(amenity.getSlotDurationMinutes() != 0){ // It is verifying that this amenity has slots in minutes
//            LocalTime slotStartAt = req.getStartAt().toLocalTime();
//            LocalTime slotEndAt = req.getEndAt().toLocalTime();
            
           int activeCount = amenityBookingRepo.nextSlotBookedAmentiy(amenityId, req.getStartAt(), req.getEndAt(),
                                  List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));
           
           if(activeCount >= amenity.getMaxCapacity()){
               log.info("Can't book this amenity no slots available for this specific data and time");
               throw new SlotFullException("Booking Failed: Amenity maximum booking capacity reached."
                       + " please try with different time");
           }
           
            Booking booking = new Booking();
            booking.setTotalAmount(amenity.getChargePerSlot());
            booking.setTenant(tenant);
            booking.setStatus(BookingStatus.PENDING);
            booking.setIsPaid(Boolean.FALSE);
            booking.setBookingType(BookingType.AMENITY);
            booking.setHoldExpireAt(LocalDateTime.now().plusMinutes(10));
           
            AmenityBooking amenityBooking = new AmenityBooking();
            amenityBooking.setAmenity(amenity);
            amenityBooking.setStartAt(req.getStartAt());
            amenityBooking.setEndAt(req.getEndAt());
            
            amenityBooking.setBooking(booking);
            booking.setAmenityBooking(amenityBooking);
            
            booking = bookingRepo.save(booking);
            log.info("Amenity booking created successfully and saved to DB");
            
            return BookingResponseMapper.mapToAmenityBookingCreateDto(booking);
        }
        
        return null;
    }
    
    private void isTenantEligible(Tenant tenant){
        log.info("Checking is tenant eligible for booking amenity");
        
        if(tenant.getFlat() == null){
            log.warn("Tenant has no assigned flats to continue amenity booking");
            throw new IllegalStateException("Tenant has no assigned flats");
        }
    }
    
    public boolean isSlotAvailable(UUID amenityId, LocalDateTime startAt, LocalDateTime endAt){
        log.info("Received request to check is slow available");
        
          Amenity amenity = amenityRepo.findById(amenityId)
                  .orElseThrow(() -> {
                      log.warn("Amenity not found with Id: {}", amenityId);
                      return new ResourceNotFoundException("amenity not found");
                  });
          
          if(endAt.isBefore(startAt) || endAt.isEqual(startAt)){
              throw new IllegalArgumentException("End time must be after start time");
          }
          
         int activeCount = amenityBookingRepo.nextSlotBookedAmentiy(amenityId, startAt, endAt,
                                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));
         
         if(!(activeCount < amenity.getMaxCapacity())){
             log.info("Given amenity: {} is not available for given specific date and time", amenityId);
             return false;
         }
         return true;
    }
    
    public List<AmenityBookingResponse> getAllAmenityBookingsByTenant(UUID tenantId, BookingStatus filter){
        log.info("Received request to get all amenity bookings with filter");
        
        tenantRepo.findById(tenantId)
                .orElseThrow(() -> {
                    log.warn("User tenant not found with Id: {}", tenantId);
                    return new ResourceNotFoundException("User not found");
                });
                
        if(filter.toString().equals("ALL")){
           return bookingRepo.findByTenantId(tenantId).stream()
                   .map(b -> BookingResponseMapper.mapToAmenityBookingDto(b)).collect(Collectors.toList());
        }
        return bookingRepo.filterAndFindAmenityBooking(tenantId, filter).stream()
                .map(b -> BookingResponseMapper.mapToAmenityBookingDto(b)).collect(Collectors.toList());
    }
    
    public List<SlotAvailabilityResponse> getAvailableSlots(UUID amenityId, LocalDate bookingDate){
        log.info("Received request to get all available slots for amenity: {}", amenityId);
          Amenity amenity = amenityRepo.findById(amenityId)
                  .orElseThrow(() -> {
                      log.warn("Amenity not found with Id: {}", amenityId);
                      return new ResourceNotFoundException("amenity not found");
                  });
          
          LocalDate currentDate = LocalDate.now();
          Long totalDays = ChronoUnit.DAYS.between(currentDate, bookingDate);
          
          
          if(amenity.getAdvanceBookingDays() < totalDays){
              log.warn("User trying to check amenity for {} days in advance."
                      + " Allowed only {} days in advance", totalDays, amenity.getAdvanceBookingDays());
              throw new InvalidBookingException("You can't book this amenity up to "
                      + totalDays + " days only " + amenity.getAdvanceBookingDays() + " days allowed in advance.");
          }
          
          LocalTime openingTime = LocalTime.of(amenity.getOpenTime().getHour(), amenity.getOpenTime().getMinute());
          LocalTime closingTime = LocalTime.of(amenity.getCloseTime().getHour(), amenity.getCloseTime().getMinute());
          
          List<SlotAvailabilityResponse> availableSlotsTime = new ArrayList<>();
          
          if(bookingDate.isEqual(currentDate)){
              log.info("User selected present date {} for all available slots", currentDate);
              LocalTime nextSlotTime = LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);              
              
              while(nextSlotTime.isBefore(closingTime)){
                  LocalDateTime startAt = LocalDateTime.of(currentDate, nextSlotTime);
                  LocalDateTime endAt = LocalDateTime.of(currentDate, nextSlotTime.plusHours(1));
                  
                  int activeCount = amenityBookingRepo.nextSlotBookedAmentiy(amenityId, startAt, endAt,
                                         List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));
                  
                  if(activeCount == amenity.getMaxCapacity()){
                      nextSlotTime = nextSlotTime.plusHours(1);
                  }else{
                      Integer availableSlotUnits = amenity.getMaxCapacity() - activeCount;
                      availableSlotsTime
                              .add(new SlotAvailabilityResponse(availableSlotUnits, nextSlotTime, nextSlotTime.plusHours(1)));
                      nextSlotTime = nextSlotTime.plusHours(1);
                  } 
              }
          }else{
              log.info("User selected future date {} for all available slots", bookingDate);
              
              LocalDateTime startAt = LocalDateTime.of(bookingDate, openingTime);
              
              while(startAt.isBefore(LocalDateTime.of(bookingDate, closingTime))){
                  int activeCount = amenityBookingRepo.nextSlotBookedAmentiy(amenityId, startAt, startAt.plusHours(1),
                                         List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));
                  
                  if(activeCount == amenity.getMaxCapacity()){
                      startAt = startAt.plusHours(1);
                  }else{
                      Integer availableSlotUnits = amenity.getMaxCapacity() - activeCount;
                      availableSlotsTime
                              .add(new SlotAvailabilityResponse(availableSlotUnits, startAt.toLocalTime(), startAt.plusHours(1).toLocalTime()));
                      startAt = startAt.plusHours(1);
                  } 
              }
          }
          
          return availableSlotsTime;
    }

}