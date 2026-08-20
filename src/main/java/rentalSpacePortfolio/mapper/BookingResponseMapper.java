package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.Booking;



public class BookingResponseMapper {
    
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