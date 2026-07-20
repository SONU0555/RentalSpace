package rentalSpacePortfolio.dto.request.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRequest {
    
@NotBlank(message = "Name is required")
@Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
private String name;

@NotBlank(message = "Description is required")
@Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
private String description;

@NotBlank(message = "Address is required")
@Size(max = 255, message = "Address must not exceed 255 characters")
private String address;

@NotBlank(message = "City is required")
@Size(max = 100, message = "City must not exceed 100 characters")
private String city;

@NotBlank(message = "State is required")
@Size(max = 100, message = "State must not exceed 100 characters")
private String state;

@NotBlank(message = "Pin code is required")
@Pattern(regexp = "^[0-9]{6}$", message = "Pin code must be exactly 6 digits")
private String pinCode;

@NotBlank(message = "Status is required")
private String status;

@NotBlank(message = "Tier is required")
private String propertyTier;

@NotNull(message = "Minimum rent is required")
@Positive(message = "Minimum rent must be greater than 0")
private Double minimumRent;

@NotNull(message = "Maximum rent is required")
@Positive(message = "Maximum rent must be greater than 0")
private Double maximumRent;

}