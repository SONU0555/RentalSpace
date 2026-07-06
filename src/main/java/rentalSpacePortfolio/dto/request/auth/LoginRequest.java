package rentalSpacePortfolio.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email formate")
    private String email;
    
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must atleast 6 characters")
    private String password;

}