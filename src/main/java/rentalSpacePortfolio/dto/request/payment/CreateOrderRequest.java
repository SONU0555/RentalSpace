package rentalSpacePortfolio.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    
    @NotBlank(message = "bookingId is required")
    private String bookingId;

    @NotBlank(message = "paymentMethod is required")
    @Pattern(regexp = "CARD|UPI|NETBANKING", message = "paymentMethod must be CARD, UPI, or NETBANKING")
    private String paymentMethod;

    @NotBlank(message = "idempotencyKey is required")
    private String idempotencyKey;

}