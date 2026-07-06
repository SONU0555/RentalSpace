package rentalSpacePortfolio.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotNull(message = "Name is required")
    private String fullName;
    
    @Email(message = "Please provide a valid email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", 
             message = "Email must be valid format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be atleast 6 characters")
    private String password;

}