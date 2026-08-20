package rentalSpacePortfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.PaymentCategory;
import rentalSpacePortfolio.enums.PaymentStatus;


@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "payments")
public class Payment extends BaseEnity{
    
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;
    
    private String orderId;       // fake gateway order ref
    private String transactionId; // fake gateway txn ref

    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory; // FLAT_BOOKING, AMENITY_BOOKING

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // INITIATED, SUCCESS, FAILED

    private String paymentMode; // CARD, UPI, NETBANKING (fake)

}