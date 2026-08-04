package rentalSpacePortfolio.dto.request.flat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlatBookingRequest {
    
    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;
    
    @NotNull(message = "Flat ID is required")
    private UUID flatId;
    
    @NotNull(message = "Property ID is required")
    private UUID propertyId;
    
    @NotNull(message = "Lease start date is required")
    @FutureOrPresent(message = "Lease start date must be today or in the future")
    private LocalDate leaseStartDate;

    @NotNull(message = "Lease duration is required")
    @Positive(message = "Lease duration must be greater than zero")
    private Integer leaseDurationMonths;

}