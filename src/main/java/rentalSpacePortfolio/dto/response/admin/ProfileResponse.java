package rentalSpacePortfolio.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String locaton;

}