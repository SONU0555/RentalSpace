package rentalSpacePortfolio.dto.response.flat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.BookingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlatBookingResponse {
    
    private UUID id;
    private String tenantName;
    private String flatNumber;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private Double totalAmount;
    private BookingStatus status;
    private Boolean isPaid;
    private LocalDate bookingDate;
    private LocalTime bookingTime;

}