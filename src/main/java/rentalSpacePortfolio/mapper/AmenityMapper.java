package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.Property;



public class AmenityMapper {
    
   public static  void mapToAmenityDto(Amenity amenity, AmenityRequest amenityData, Property property){
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

}