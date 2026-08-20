package rentalSpacePortfolio.service.interfaces;

import rentalSpacePortfolio.dto.response.payment.OrderResponse;


public interface PaymentGateway {
    OrderResponse createOrder(String bookingId, Double amount);
    void verifyPayment(String orderId, String transactionId);
}
