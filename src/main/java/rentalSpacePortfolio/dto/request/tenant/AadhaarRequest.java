package rentalSpacePortfolio.dto.request.tenant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequest {
    
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invlid phone number")
    private String phone;
    
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

}