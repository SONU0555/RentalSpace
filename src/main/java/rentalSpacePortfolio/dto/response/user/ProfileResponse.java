package rentalSpacePortfolio.dto.response.user;

import lombok.Data;


@Data
public class ProfileResponse {
    
    private String fullName;
    private String email;
    private String role;

}