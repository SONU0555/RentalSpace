package rentalSpacePortfolio.dto.response.tenant;

//import RentSpace.Booking.dto.Response.BookingResponseDto;
//import RentSpace.property.Dto.Response.PropertyResponseDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.dto.response.user.UserSummaryResponse;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
public class TenantSummaryResponse extends UserSummaryResponse{
    
    private String address;
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;
//    private List<PropertyResponseDto> properties;
//    private List<BookingResponseDto> bookings;
    private String emergencyContect;

}