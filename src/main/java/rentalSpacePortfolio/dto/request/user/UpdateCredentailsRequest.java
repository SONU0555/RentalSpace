package rentalSpacePortfolio.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCredentailsRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email formate")
    private String email;
    
    @NotBlank(message = "Old password is required")
    @Size(min = 6, message = "Password must be atleast 6 characters")
    private String oldPassword;
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be atleast 6 characters")
    private String newPassword;

}