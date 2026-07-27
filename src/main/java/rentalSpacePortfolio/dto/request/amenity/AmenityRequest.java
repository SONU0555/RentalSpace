package rentalSpacePortfolio.dto.request.amenity;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.AmenityStatus;
import rentalSpacePortfolio.enums.AmenityType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityRequest {
    
    @NotBlank(message = "Amenity name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Amenity type is required")
    private AmenityType type;

    @NotNull(message = "Amenity status is required")
    private AmenityStatus status;

    @NotNull(message = "Maximum capacity is required")
    @Positive(message = "Maximum capacity must be greater than zero")
    private Integer maxCapacity;

    @NotNull(message = "Opening time is required")
    private LocalTime openTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closeTime;

    @NotNull(message = "Slot duration is required")
    @Positive(message = "Slot duration must be greater than zero")
    @Min(value = 5, message = "Slot duration must be at least 5 minutes")
    private Integer slotDurationMinutes;

    @NotNull(message = "Paid status flag is required")
    private Boolean isPaid;

    @NotNull(message = "Charge per slot is required")
    @PositiveOrZero(message = "Charge per slot cannot be negative")
    private Double chargePerSlot;

    @NotNull(message = "Maximum slots per tenant is required")
    @Positive(message = "Maximum slots per tenant must be greater than zero")
    private Integer maxSlotsPerTenant;

    @NotNull(message = "Advance booking days restriction is required")
    @PositiveOrZero(message = "Advance booking days cannot be negative")
    private Integer advanceBookingDays;

    // Custom Validation Cross-Check Method
    @AssertTrue(message = "Closing time must be after opening time")
    public boolean isTimeWindowValid() {
        return closeTime.isAfter(openTime);
    }

}