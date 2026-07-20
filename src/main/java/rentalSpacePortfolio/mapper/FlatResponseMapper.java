package rentalSpacePortfolio.mapper;

import java.util.stream.Collectors;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.dto.response.flat.ImageResponse;
import rentalSpacePortfolio.entity.Flat;



public class FlatResponseMapper {
    
    public static FlatResponse mapToFlatDto(Flat request){
        FlatResponse response = new FlatResponse();
        response.setId(request.getId());
        response.setBuildingName(request.getBuildingName());
        response.setFlatNumber(request.getFlatNumber());
        response.setFloorNumber(request.getFloorNumber());
        response.setAreaSqFt(request.getAreaSqFt());
        response.setRentAmount(request.getRentAmount());
        response.setStatus(request.getStatus());
        response.setType(request.getType());
        response.setFlatImages(request.getFlatImages().stream().map(img -> 
                new ImageResponse(img.getId(), img.getImageDetails())).collect(Collectors.toList()));
        
        return response;
    }

}