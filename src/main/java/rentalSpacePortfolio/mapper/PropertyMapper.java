package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.User;


public class PropertyMapper {
    
    // shared method to map dto to entity for property
    public static void mapToPropertyDto(Property property, PropertyRequest propertyData, User owner){
        property.setName(propertyData.getName());
        property.setDescription(propertyData.getDescription());
        property.setAddress(propertyData.getAddress());
        property.setCity(propertyData.getCity());
        property.setState(propertyData.getState());
        property.setPinCode(propertyData.getPinCode());
        if(owner != null){
            property.setOwner(owner);
        }
        property.setStatus(propertyData.getStatus());
        property.setTier(propertyData.getPropertyTier());
        property.setMinimumRent(propertyData.getMinimumRent());
        property.setMaximumRent(propertyData.getMaximumRent());        
    }

}