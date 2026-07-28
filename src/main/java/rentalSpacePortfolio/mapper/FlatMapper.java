package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.request.flat.FlatDataRequest;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.enums.FlatStatus;



public class FlatMapper {
    
    // shared method to map flat dto to entity
    public static void mapToFlatDto(Flat flat, FlatDataRequest flatData, Property property){
        flat.setBuildingName(flatData.getBuildingName());
        flat.setFlatNumber(flatData.getFlatNumber());
        flat.setFloorNumber(flatData.getFloorNumber());
        flat.setAreaSqFt(flatData.getAreaSqFt());
        flat.setType(flatData.getType());
        flat.setStatus(flatData.getStatus());
        flat.setRentAmount(flatData.getRentAmount());
        flat.setProperty(property == null ? flat.getProperty() : property);
        flat.setStatus(FlatStatus.ACTIVE);
    }

}