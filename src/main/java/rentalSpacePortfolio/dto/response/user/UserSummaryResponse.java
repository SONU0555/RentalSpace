package rentalSpacePortfolio.dto.response.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserSummaryResponse {
    
    private String id;
    private String full_name;
    private String email;
    private String phone;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}