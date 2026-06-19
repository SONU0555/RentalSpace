package rentalSpacePortfolio.dto.request.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Optional;


public class ProfileUpdateRequest{
    
    private Optional<String> full_name = Optional.empty();
    
    @Email(message = "Please provide a valid email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", 
             message = "Email must be valid format")
    private Optional<String> email = Optional.empty();
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private Optional<String> phone  = Optional.empty();
    
//    @NotBlank(message = "Aadhaar number is required")
//    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number")
//    private String aadhaarNumber;
//    
//    @NotBlank(message = "Emergency contact is required")
//    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid emergency contact number")    
//    private Optional<String> emergencyContect  = Optional.empty();  

    public Optional<String> getFull_name() {
        return full_name;
    }

    public void setFull_name(Optional<String> full_name) {
        this.full_name = full_name;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getPhone() {
        return phone;
    }

    public void setPhone(Optional<String> phone) {
        this.phone = phone;
    }

//    public Optional<String> getEmergencyContect() {
//        return emergencyContect;
//    }
//
//    public void setEmergencyContect(Optional<String> emergencyContect) {
//        this.emergencyContect = emergencyContect;
//    }
//  
    

}