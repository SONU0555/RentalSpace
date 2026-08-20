package rentalSpacePortfolio.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    
    private String orderId;
    private String bookingId;
    private Double amount;
    private String currency;

}