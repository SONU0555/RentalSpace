package rentalSpacePortfolio.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.dto.response.user.UserSummaryResponse;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
public class AdminSummaryResponse extends UserSummaryResponse{
    
    private Long property_assigned;
    private String location;
    private String employeeCode;

}