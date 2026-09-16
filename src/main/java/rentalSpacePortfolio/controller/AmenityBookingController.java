package rentalSpacePortfolio.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.response.Amenity.SlotAvailabilityResponse;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.service.impl.AmenityBookingService;


@RestController
@RequestMapping(ApiPaths.BASE + "/booking/amenities")
public class AmenityBookingController {
    
    private final AmenityBookingService amenityBookingService;
    
    @Autowired
    public AmenityBookingController(AmenityBookingService amenityBookingService){
        this.amenityBookingService = amenityBookingService;
    }
    
    @GetMapping("/{amenityId}/isAvailable")
    public ResponseEntity<Boolean> isSlotAvailable(@PathVariable UUID amenityId,
            @RequestParam("startAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAt,
            @RequestParam("endAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endAt){
        
       boolean isAvailable = amenityBookingService.isSlotAvailable(amenityId, startAt, endAt);
       return ResponseEntity.ok(isAvailable);
    }
    
    @GetMapping("/{amenityId}/slots")
    public ResponseEntity<ApiResponse<List<SlotAvailabilityResponse>>> getAllAvailableSlots(
            @PathVariable("amenityId") UUID amenityId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate){
        
        List<SlotAvailabilityResponse> listOfAvailableSlots = amenityBookingService.getAvailableSlots(amenityId, bookingDate);
        return new ResponseEntity<>((new ApiResponse<>(true, "List of available slots Time", listOfAvailableSlots)), HttpStatus.OK);
    }

}