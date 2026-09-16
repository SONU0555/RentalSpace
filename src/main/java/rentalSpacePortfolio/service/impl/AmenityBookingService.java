package rentalSpacePortfolio.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.response.Amenity.SlotAvailabilityResponse;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.AmenityBooking;
import rentalSpacePortfolio.exception.InvalidBookingException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.repository.AmenityBookingRepository;
import rentalSpacePortfolio.repository.AmenityRepository;


@Service
@Slf4j
public class AmenityBookingService {
    
    private final AmenityBookingRepository amenityBookingRepo;
    private final AmenityRepository amenityRepo;
    
    @Autowired
    public AmenityBookingService(
            AmenityBookingRepository amenityBookingRepo,
            AmenityRepository amenityRepo){
        this.amenityBookingRepo = amenityBookingRepo;
        this.amenityRepo = amenityRepo;
    }
    
    public void createAmenityBooking(){
        
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
          
         List<AmenityBooking> totalBookings = amenityBookingRepo.findAllBookedAmenities(amenityId, startAt, endAt);
         
         if(!(totalBookings.size() < amenity.getMaxCapacity())){
             log.info("Given amenity: {} is not available for given specific date and time", amenityId);
             return false;
         }
         
         return true;
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
              log.warn("User trying to book amenity for {} days in advance."
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
              LocalDateTime startAt = LocalDateTime.of(currentDate, nextSlotTime);
              LocalDateTime endAt = LocalDateTime.of(currentDate, nextSlotTime.plusHours(1));
              
              
              while(nextSlotTime.isBefore(closingTime)){
                  List<AmenityBooking> totalBookings = amenityBookingRepo
                          .findAllBookedAmenities(amenityId, startAt, endAt);
                  
                  if(totalBookings.size() == amenity.getMaxCapacity()){
                      nextSlotTime = nextSlotTime.plusHours(1);
                  }else{
                      Integer availableSlotUnits = amenity.getMaxCapacity() - totalBookings.size();
                      availableSlotsTime
                              .add(new SlotAvailabilityResponse(availableSlotUnits, nextSlotTime, nextSlotTime.plusHours(1)));
                      nextSlotTime = nextSlotTime.plusHours(1);
                  } 
              }
          }else{
              log.info("User selected future date {} for all available slots", bookingDate);
              
              LocalDateTime startAt = LocalDateTime.of(bookingDate, openingTime);
              
              while(startAt.isBefore(LocalDateTime.of(bookingDate, closingTime))){
                  
                  List<AmenityBooking> totalBookings = amenityBookingRepo
                          .findAllBookedAmenities(amenityId, startAt, startAt.plusHours(1));
                  
                  if(totalBookings.size() == amenity.getMaxCapacity()){
                      startAt = startAt.plusHours(1);
                  }else{
                      Integer availableSlotUnits = amenity.getMaxCapacity() - totalBookings.size();
                      availableSlotsTime
                              .add(new SlotAvailabilityResponse(availableSlotUnits, startAt.toLocalTime(), startAt.plusHours(1).toLocalTime()));
                      startAt = startAt.plusHours(1);
                  } 
              }
          }
          
          return availableSlotsTime;
       
    }

}