package rentalSpacePortfolio.dto.response.Amenity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.BookingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityBookingCreateResponse {
    
    private UUID id;
    private String tenantName;
    private String amenityName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Double totalAmount;
    private BookingStatus status;
    private Boolean isPaid;
//    private LocalDate bookingDate;
//    private LocalTime bookingTime;
    
}