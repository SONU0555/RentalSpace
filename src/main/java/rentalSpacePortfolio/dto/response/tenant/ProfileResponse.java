package rentalSpacePortfolio.dto.response.tenant;

import lombok.Data;


@Data
public class ProfileResponse {
    
    private String full_name;
    private String email;
    private String phone;
    private String emergencyContect;
    private String isVarified;
    private String role;

}