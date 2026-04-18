package RentSpace.dto.requestDto;

import RentSpace.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class UserDetailsRequestDto {
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private String phone;
    
    //Tenant fiels
    @NotBlank(message = "Address is requeired")
    @Size(max = 225, message = "Address must not exceed 255 characters")
    private String address;
    
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number")
    private String aadhaarNumber;
    
    @NotNull(message = "Rent end date is required")
    @Future(message = "Rent end date must be in future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentEndDate;
    
    @NotBlank(message = "Emergency contact is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid emergency contact number")    
    private String emergencyContect;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public LocalDate getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(LocalDate rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public String getEmergencyContect() {
        return emergencyContect;
    }

    public void setEmergencyContect(String emergencyContect) {
        this.emergencyContect = emergencyContect;
    }
    
    

}