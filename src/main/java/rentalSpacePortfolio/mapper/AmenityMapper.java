package rentalSpacePortfolio.mapper;

import java.util.stream.Collectors;
import rentalSpacePortfolio.dto.image.ImageResponse;
import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.dto.response.Amenity.AmenityResponse;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.Property;



public class AmenityMapper {
    
   public static void mapToAmenityDto(Amenity amenity, AmenityRequest amenityData, Property property){
        amenity.setName(amenityData.getName());
        amenity.setDescription(amenityData.getDescription());
        amenity.setType(amenityData.getType());
        amenity.setStatus(amenityData.getStatus());
        amenity.setMaxCapacity(amenityData.getMaxCapacity());
        amenity.setOpenTime(amenityData.getOpenTime());
        amenity.setCloseTime(amenityData.getCloseTime());
        amenity.setSlotDurationMinutes(amenityData.getSlotDurationMinutes());
        amenity.setProperty(property == null ? amenity.getProperty() : property);
        amenity.setIsPaid(amenityData.getIsPaid());
        amenity.setChargePerSlot(amenityData.getChargePerSlot());
        amenity.setMaxSlotsPerTenant(amenityData.getMaxSlotsPerTenant());
        amenity.setAdvanceBookingDays(amenityData.getAdvanceBookingDays());
        
    }
   
   public static AmenityResponse mapToAmenityResponseDto(Amenity request){
       AmenityResponse response = new AmenityResponse();
       response.setId(request.getId());
       response.setName(request.getName());
       response.setDescription(request.getDescription());
       response.setType(request.getType());
       response.setStatus(request.getStatus());
       response.setMaxCapacity(request.getMaxCapacity());
       response.setOpenTime(request.getOpenTime());
       response.setCloseTime(request.getCloseTime());
       response.setSlotDurationMinutes(request.getSlotDurationMinutes());
       response.setIsPaid(request.getIsPaid());
       response.setChargePerSlot(request.getChargePerSlot());
       response.setMaxSlotsPerTenant(request.getMaxSlotsPerTenant());
       response.setAdvanceBookingDays(request.getAdvanceBookingDays());
       response.setAmenityImages(request.getAmenityImages().stream().map(img -> 
                new ImageResponse(img.getId(), img.getImageDetails())).collect(Collectors.toList()));
       
       return response;
   }

}