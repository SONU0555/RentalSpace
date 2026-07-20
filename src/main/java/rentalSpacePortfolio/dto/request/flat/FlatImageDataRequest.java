package rentalSpacePortfolio.dto.request.flat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatImageDataRequest {
    
    private UUID id;

    @NotNull(message = "isCoverImage must be true or false — it cannot be missing")
    private Boolean isCoverImage;

    @NotNull(message = "Display order is required")
    @Min(value = 1,  message = "Display order must be at least 1")
    @Max(value = 5, message = "Display order must not exceed 20")
    private Integer displayOrder; 

}