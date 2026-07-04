package rentalSpacePortfolio.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.PropertyImage;




public class PropertyResponseMapper {
    
    public static final Logger logger = LoggerFactory.getLogger(PropertyResponseMapper.class);
    
    public static PropertyResponse mapToPropertyResponse(Property request){
        
        logger.info("Requeted to Map Property Enity to PropertyResponseDto");
        
        PropertyResponse response = new PropertyResponse();
        response.setId(request.getId().toString());
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setCity(request.getCity());
        response.setState(request.getState());
        response.setStatus(request.getStatus().toString());
        response.setCoverImage(request.getPropertyImages().stream()
                .filter(PropertyImage::getIsCoverImage)
                .map(PropertyImage::getImageUrl)
                .findFirst()
                .orElse(null));
        response.setMiniumRent(request.getMiniumRent());
        response.setMaximumRent(request.getMaximumRent());
        
        logger.info("Property Enity mapped succesfully into PropertyResponseDto");
        
        return response;
    }

}