package rentalSpacePortfolio.dto.response.admin;

import lombok.Data;


@Data
public class ProfileResponse {
    
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String locaton;

}