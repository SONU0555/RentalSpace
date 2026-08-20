package rentalSpacePortfolio.dto.response.payment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {
    
    private String orderId;
    private String transactionId;
    private String status;       // "SUCCESS" or "FAILED"
    private LocalDateTime paidAt;
    
}