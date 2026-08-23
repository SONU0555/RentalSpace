package rentalSpacePortfolio.mapper;

import java.util.List;
import java.util.stream.Collectors;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.dto.image.ImageResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.Booking;
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
    
    public static FlatBookingResponse mapToFlatBookingDto(Booking parentReq, FlatBooking childReq){
        FlatBookingResponse response = new FlatBookingResponse();
        response.setId(parentReq.getId());
        response.setTenantName(parentReq.getTenant().getUser().getFull_name());
        response.setFlatNumber(childReq.getFlat().getFlatNumber());
        response.setLeaseStartDate(childReq.getLeaseStartDate());
        response.setLeaseEndDate(childReq.getLeaseEndDate());
        response.setTotalAmount(childReq.getMonthlyRent() + childReq.getSecurityDeposit());
        response.setStatus(parentReq.getStatus());
        response.setBookingDate(parentReq.getCreatedAt().toLocalDate());
        response.setBookingTime(parentReq.getCreatedAt().toLocalTime());
        
        return response;
    }
    
    public static FlatBookingResponse mapToBookingHistoryResponseDto(Booking request){
        FlatBookingResponse response = new FlatBookingResponse();
        response.setId(request.getId());
        response.setTenantName(request.getTenant().getUser().getFull_name());
        response.setFlatNumber(request.getFlatBooking().getFlat().getFlatNumber());
        response.setLeaseStartDate(request.getFlatBooking().getLeaseStartDate());
        response.setLeaseEndDate(request.getFlatBooking().getLeaseEndDate());
        response.setTotalAmount(request.getFlatBooking().getMonthlyRent() + request.getFlatBooking().getSecurityDeposit());
        response.setStatus(request.getStatus());
        response.setIsPaid(request.getIsPaid());
        response.setBookingDate(request.getCreatedAt().toLocalDate());
        response.setBookingTime(request.getCreatedAt().toLocalTime());
        
        return response;
    }

}