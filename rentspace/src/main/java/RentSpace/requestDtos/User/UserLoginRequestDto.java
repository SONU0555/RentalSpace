package RentSpace.requestDtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserLoginRequestDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email formate")
    private String email;
    
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must atleast 6 characters")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

}