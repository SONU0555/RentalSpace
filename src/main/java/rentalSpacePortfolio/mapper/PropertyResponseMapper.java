package rentalSpacePortfolio.mapper;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rentalSpacePortfolio.dto.response.property.ImageResponse;
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
        response.setImages(mapToPropertyImageResponse(request.getPropertyImages()));
        response.setMiniumRent(request.getMinimumRent());
        response.setMaximumRent(request.getMaximumRent());
        
        logger.info("Property Enity mapped succesfully into PropertyResponseDto");
        
        return response;
    }
    
    private static List<ImageResponse> mapToPropertyImageResponse(List<PropertyImage> images){
        
        List<ImageResponse> responseList = new ArrayList<>();
        
        for(PropertyImage img : images){
            ImageResponse response = new ImageResponse();
            response.setId(img.getId());
            response.setImageUrl(img.getImageUrl());
            response.setDisplayOrder(img.getDisplayOrder());
            response.setIsCoverImage(img.getIsCoverImage());
            
            responseList.add(response);
        }
        
        return responseList;
    }

}