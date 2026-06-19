package rentalSpacePortfolio.dto.response.tenant;

//import RentSpace.Booking.dto.Response.BookingResponseDto;
//import RentSpace.property.Dto.Response.PropertyResponseDto;
import java.time.LocalDate;
import java.util.List;
import rentalSpacePortfolio.dto.response.user.UserSummaryResponse;


public class TenantSummaryResponse extends UserSummaryResponse{
    
    private String address;
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;
//    private List<PropertyResponseDto> properties;
//    private List<BookingResponseDto> bookings;
    private String emergencyContect;
     

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(LocalDate rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDate getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(LocalDate rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

//    public List<PropertyResponseDto> getProperties() {
//        return properties;
//    }

//    public List<BookingResponseDto> getBookings() {
//        return bookings;
//    }
//
//    public void setBookings(List<BookingResponseDto> bookings) {
//        this.bookings = bookings;
//    }

//    public void setProperties(List<PropertyResponseDto> properties) {
//        this.properties = properties;
//    }

    public String getEmergencyContect() {
        return emergencyContect;
    }

    public void setEmergencyContect(String emergencyContect) {
        this.emergencyContect = emergencyContect;
    }
     

}