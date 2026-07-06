package rentalSpacePortfolio.dto.request.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import lombok.Data;

@Data
public class ProfileUpdateRequest{
    
    @NotBlank(message = "Name is required")
    private String full_name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", 
             message = "Email must be valid format")
    private String email;
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private Optional<String> phone  = Optional.empty();
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private Optional<String> emergencyContect  = Optional.empty();
    
//    @NotBlank(message = "Aadhaar number is required")
//    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number")
//    private String aadhaarNumber;

}