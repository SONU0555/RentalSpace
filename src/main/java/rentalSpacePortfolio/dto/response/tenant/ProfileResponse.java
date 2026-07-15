package rentalSpacePortfolio.dto.response.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    
    private String full_name;
    private String email;
    private String phone;
    private String emergencyContect;
    private String isVarified;
    private String role;

}