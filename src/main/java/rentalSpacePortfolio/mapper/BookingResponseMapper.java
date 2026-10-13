package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.response.Amenity.AmenityBookingCreateResponse;
import rentalSpacePortfolio.dto.response.Amenity.AmenityBookingResponse;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.entity.AmenityBooking;
import rentalSpacePortfolio.entity.Booking;
import rentalSpacePortfolio.entity.FlatBooking;



public class BookingResponseMapper {
    
    public static FlatBookingResponse mapToBookingHistoryResponseDto(Booking request){
        FlatBooking flatBooking = request.getFlatBooking();
        FlatBookingResponse response = new FlatBookingResponse();
        response.setId(request.getId());
        response.setTenantName(request.getTenant().getUser().getFull_name());
        response.setFlatNumber(flatBooking.getFlat().getFlatNumber());
        response.setLeaseStartDate(flatBooking.getLeaseStartDate());
        response.setLeaseEndDate(flatBooking.getLeaseEndDate());
        response.setTotalAmount(flatBooking.getMonthlyRent() + request.getFlatBooking().getSecurityDeposit());
        response.setStatus(request.getStatus());
        response.setIsPaid(request.getIsPaid());
        response.setBookingDate(request.getCreatedAt().toLocalDate());
        response.setBookingTime(request.getCreatedAt().toLocalTime());
        
        return response;
    }
    
    public static AmenityBookingCreateResponse mapToAmenityBookingCreateDto(Booking req){
        AmenityBooking amenityBooking = req.getAmenityBooking();
        AmenityBookingCreateResponse res = new AmenityBookingCreateResponse();
        res.setId(req.getId());
        res.setAmenityName(amenityBooking.getAmenity().getName());
        res.setStartAt(amenityBooking.getStartAt());
        res.setEndAt(amenityBooking.getEndAt());
        res.setTotalAmount(req.getTotalAmount());
        res.setStatus(req.getStatus());
        res.setIsPaid(req.getIsPaid());
        return res;
    }
    
    public static AmenityBookingResponse mapToAmenityBookingDto(Booking req){
        AmenityBooking amenityBooking = req.getAmenityBooking();
        AmenityBookingResponse res = new AmenityBookingResponse();
        res.setId(req.getId());
        res.setAmenityName(amenityBooking.getAmenity().getName());
        res.setStartAt(amenityBooking.getStartAt());
        res.setEndAt(amenityBooking.getEndAt());
        res.setTotalAmount(req.getTotalAmount());
        res.setStatus(req.getStatus());
        res.setIsPaid(req.getIsPaid());
        res.setBookingDateTime(req.getCreatedAt());
        
        return res;
    }

}