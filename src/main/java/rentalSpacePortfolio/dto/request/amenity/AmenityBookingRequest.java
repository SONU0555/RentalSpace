package rentalSpacePortfolio.dto.request.amenity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmenityBookingRequest {
    
//    @NotNull(message = "No. of slots required")
//    private Integer noOfSlots;
    
    @NotNull(message = "start date is required")
    @Future(message = "startAt must be in futrue")
    private LocalDateTime startAt;
    
    @NotNull(message = "start date is required")
    @Future(message = "startAt must be in futrue")
    private LocalDateTime endAt;

}