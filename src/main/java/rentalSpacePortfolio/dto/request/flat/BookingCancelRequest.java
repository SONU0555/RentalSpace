package rentalSpacePortfolio.dto.request.flat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancelRequest {
    
    @NotBlank(message = "Reason is required")
    @Size(min = 3, max = 100, message = "Reason must be between 3 and 100 characters")
    private String cancellationReason;

}