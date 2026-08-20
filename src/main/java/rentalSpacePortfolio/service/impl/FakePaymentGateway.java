package rentalSpacePortfolio.service.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rentalSpacePortfolio.dto.request.payment.VerifyPaymentRequest;
import rentalSpacePortfolio.dto.response.payment.OrderResponse;
import rentalSpacePortfolio.service.interfaces.PaymentGateway;


@Component
@Slf4j
public class FakePaymentGateway implements PaymentGateway{
    
    private final RestTemplate restTemplate;
    
    @Autowired
    public FakePaymentGateway(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public OrderResponse createOrder(String bookingId, Double amount) {
        String orderId = "order_" + UUID.randomUUID();
        log.info("Fake gateway: created order {} for booking {}", orderId, bookingId);
        return new OrderResponse(orderId, bookingId, amount, "INR");
    }

    @Override
    @Async
    public void verifyPayment(String orderId, String transactionId) {
        // Simulate processing delay
        try { 
            log.info("Processing payment...");
            Thread.sleep(1500); 
        } catch (InterruptedException ignored) {}

        // If caller forces an outcome (for testing)
        boolean success = new Random().nextInt(10) < 9; // 90% success rate
        
        VerifyPaymentRequest webhookPayload = new VerifyPaymentRequest();
        webhookPayload.setOrderId(orderId);
        webhookPayload.setTransactionId(transactionId);
        webhookPayload.setSimulatedStatus(success ? "SUCCESS" : "FAILED");
        webhookPayload.setPaidAt(LocalDateTime.now());
        
        restTemplate.postForObject("http://localhost:8080/api/payments/webhook/payment-callback", 
        webhookPayload, Void.class);
    }

}