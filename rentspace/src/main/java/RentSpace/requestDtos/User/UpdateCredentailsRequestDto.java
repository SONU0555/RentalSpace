package RentSpace.requestDtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UpdateCredentailsRequestDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email formate")
    private String email;
    
    @NotBlank(message = "Old password is required")
    @Size(min = 6, message = "Password must be atleast 6 characters")
    private String oldPassword;
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be atleast 6 characters")
    private String newPassword;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    

}