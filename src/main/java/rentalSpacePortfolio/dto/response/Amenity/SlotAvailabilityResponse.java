package rentalSpacePortfolio.dto.response.Amenity;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotAvailabilityResponse {
    
    private Integer numberOfSlotsAvailable;
    private LocalTime slotStartAt;
    private LocalTime slotEndAt;

}