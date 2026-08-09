package rentalSpacePortfolio.mapper;

import java.util.List;
import java.util.stream.Collectors;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.dto.image.ImageResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.FlatBooking;



public class FlatResponseMapper {
    
    public static FlatResponse mapToFlatDto(Flat request){
        FlatResponse response = new FlatResponse();
        response.setId(request.getId());
        response.setBuildingName(request.getBuildingName());
        response.setFlatNumber(request.getFlatNumber());
        response.setFloorNumber(request.getFloorNumber());
        response.setAreaSqFt(request.getAreaSqFt());
        response.setRentAmount(request.getRentAmount());
        response.setSecurityDeposit(request.getSecurityDeposit());
        response.setStatus(request.getStatus());
        response.setType(request.getType());
        response.setFlatImages(request.getFlatImages().stream().map(img -> 
                new ImageResponse(img.getId(), img.getImageDetails())).collect(Collectors.toList()));
        
        return response;
    }
    
    public static FlatBookingResponse mapToFlatBookingDto(FlatBooking request){
        FlatBookingResponse response = new FlatBookingResponse();
        response.setId(request.getId());
        response.setTenantName(request.getTenant().getUser().getFull_name());
        response.setFlatNumber(request.getFlat().getFlatNumber());
        response.setLeaseStartDate(request.getLeaseStartDate());
        response.setLeaseEndDate(request.getLeaseEndDate());
        response.setTotalAmount(request.getMonthlyRent() + request.getSecurityDeposit());
        response.setStatus(request.getStatus());
        
        return response;
    }
    
    public static FlatBookingResponse mapToBookingHistoryResponseDto(FlatBooking request){
        FlatBookingResponse response = new FlatBookingResponse();
        response.setId(request.getId());
        response.setTenantName(request.getTenant().getUser().getFull_name());
        response.setFlatNumber(request.getFlat().getFlatNumber());
        response.setLeaseStartDate(request.getLeaseStartDate());
        response.setLeaseEndDate(request.getLeaseEndDate());
        response.setTotalAmount(request.getMonthlyRent() + request.getSecurityDeposit());
        response.setStatus(request.getStatus());
        response.setBookingDate(request.getCreatedAt().toLocalDate());
        response.setBookingTime(request.getCreatedAt().toLocalTime());
        
        return response;
    }

}