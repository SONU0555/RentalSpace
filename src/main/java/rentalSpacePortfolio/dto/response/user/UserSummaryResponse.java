package rentalSpacePortfolio.dto.response.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    
    private String id;
    private String full_name;
    private String email;
    private String phone;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}