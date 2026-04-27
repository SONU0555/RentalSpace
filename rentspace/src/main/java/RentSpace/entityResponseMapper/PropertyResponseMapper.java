package RentSpace.entityResponseMapper;

import RentSpace.entity.Property;
import RentSpace.responseDto.PropertyResponseDto;


public class PropertyResponseMapper {
    
    public static PropertyResponseDto mapToPropertyResponse(Property property){
        PropertyResponseDto response = new PropertyResponseDto();
        response.setId(property.getId());
        response.setTitle(property.getTitle());
        response.setDescription(property.getDescription());
        response.setLocation(property.getLocation());
        response.setPrice(property.getPrice());
        response.setStatus(property.getStatus());
        response.setOwner(property.getOwner().getId());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());
        
        return response;
    }

}