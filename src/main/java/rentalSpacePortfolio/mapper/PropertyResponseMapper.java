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
                
        PropertyResponse response = new PropertyResponse();
        response.setId(request.getId().toString());
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setCity(request.getCity());
        response.setState(request.getState());
        response.setStatus(request.getStatus().toString());
        response.setPropertyTier(request.getTier());
        response.setImages(mapToPropertyImageResponse(request.getPropertyImages()));
        response.setMiniumRent(request.getMinimumRent());
        response.setMaximumRent(request.getMaximumRent());
                
        return response;
    }
    
    private static List<ImageResponse> mapToPropertyImageResponse(List<PropertyImage> images){
        
        List<ImageResponse> responseList = new ArrayList<>();
        
        for(PropertyImage img : images){
            ImageResponse response = new ImageResponse();
            response.setId(img.getId());
            response.setImageUrl(img.getImageDetails().getImageUrl());
            response.setDisplayOrder(img.getImageDetails().getDisplayOrder());
            response.setIsCoverImage(img.getImageDetails().getIsCoverImage());
            
            responseList.add(response);
        }
        
        return responseList;
    }

}