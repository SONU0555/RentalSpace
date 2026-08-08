package rentalSpacePortfolio.dto.request.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest{
    
    @NotBlank(message = "Name is required")
    private String full_name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", 
             message = "Email must be valid format")
    private String email;
    
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private String phone;
    
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private String emergencyContect;

}