package rentalSpacePortfolio.dto.request.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequest{
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private String phone;
    
    @NotBlank(message = "Address is requeired")
    @Size(max = 225, message = "Address must not exceed 255 characters")
    private String address;
    
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number")
    private String aadhaarNumber;
    
    @NotBlank(message = "Emergency contact is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid emergency contact number")    
    private String emergencyContect;    
    
    @NotBlank(message = "Company name is required")
    private String companyName;

}