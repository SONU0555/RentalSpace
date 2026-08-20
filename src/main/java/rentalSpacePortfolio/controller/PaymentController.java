package rentalSpacePortfolio.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.payment.CreateOrderRequest;
import rentalSpacePortfolio.dto.request.payment.VerifyPaymentRequest;
import rentalSpacePortfolio.dto.response.flat.FlatBookingResponse;
import rentalSpacePortfolio.dto.response.payment.OrderResponse;
import rentalSpacePortfolio.service.impl.PaymentService;


@RestController
@RequestMapping(ApiPaths.BASE + "/payments")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest req) {
        return ResponseEntity.ok(paymentService
                .createOrder(UUID.fromString(req.getBookingId()), req.getPaymentMethod(), req.getIdempotencyKey()));
    }

//  simulatedStatus is optional - only for your own testing (e.g. force "FAILED")
    @PostMapping("/webhook/payment-callback")
    public ResponseEntity<FlatBookingResponse> verifyPayment(@RequestBody VerifyPaymentRequest req) {
        System.out.println("callback request: " + req.getSimulatedStatus());
        return ResponseEntity.ok(paymentService.verifyAndFinalize(req));
    }

}