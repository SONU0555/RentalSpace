package rentalSpacePortfolio.dto.request.flat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatDataRequest {

    @NotBlank(message = "Building name is required")
    @Size(max = 20, message = "Building name must not exceed 20 characters")
    private String buildingName;
    
    @NotBlank(message = "Flat number is required")
    @Size(max = 20, message = "Flat number must not exceed 20 characters")
    private String flatNumber;

    @NotNull(message = "Floor number is required")
    @Min(value = 0, message = "Floor number cannot be negative") // 0 handles Ground Floor
    private Integer floorNumber;

    @NotBlank(message = "Flat type is required")
    @Size(max = 50, message = "Flat type must not exceed 50 characters") // e.g., "2BHK", "Studio"
    private String type;

    @NotNull(message = "Area in square feet is required")
    @Positive(message = "Area must be greater than zero")
    private Double areaSqFt;

    @NotNull(message = "Rent amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Rent amount must be greater than zero")
    private Double rentAmount;
    
    @NotNull(message = "Flat status is required")
    private String status;

}