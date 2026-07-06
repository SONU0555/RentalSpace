package rentalSpacePortfolio.dto.request.property;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PropertyImageRequest {

    @NotNull(message = "isCoverImage must be true or false — it cannot be missing")
    private Boolean isCoverImage;

    @NotNull(message = "Display order is required")
    @Min(value = 1,  message = "Display order must be at least 1")
    @Max(value = 5, message = "Display order must not exceed 20")
    private Integer displayOrder; 

}