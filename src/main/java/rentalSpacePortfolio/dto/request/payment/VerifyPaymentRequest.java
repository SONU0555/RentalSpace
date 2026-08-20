package rentalSpacePortfolio.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPaymentRequest {
    
    @NotBlank(message = "orderId is required")
    private String orderId;

    @NotBlank(message = "transactionId is required")
    private String transactionId;

    // optional — only used for dev/testing to force an outcome
    @Pattern(regexp = "SUCCESS|FAILED", message = "simulatedStatus must be SUCCESS or FAILED")
    private String simulatedStatus;
    
    @NotNull(message = "Payment timestamp cannot be null")
    @PastOrPresent(message = "Payment timestamp must be in the past or the current time")
    private LocalDateTime paidAt;

}