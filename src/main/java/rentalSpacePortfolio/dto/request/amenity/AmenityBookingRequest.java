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
    
    @NotNull(message = "tenantId is required")
    private String tenantId;
    
    @NotNull(message = "amenityId is required")
    private String amenityId;
    
    @NotNull(message = "start date is required")
    @Future(message = "startAt must be in futrue")
    private LocalDateTime startAt;
    
    @NotNull(message = "end date is required")
    @Future(message = "endAt must be in future")
    private LocalDateTime endAt;

}