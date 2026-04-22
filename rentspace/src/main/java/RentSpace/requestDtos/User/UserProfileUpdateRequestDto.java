package RentSpace.requestDtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class UserProfileUpdateRequestDto extends OwnerDetailsRequestDto{
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email formate")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String user) {
        this.name = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}