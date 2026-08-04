package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.FlatBookingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flat_bookings")
public class FlatBooking extends BaseEnity{
    
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // Kya book kar raha hai
    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    // Kis property ka part hai
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // Lease Details
    private LocalDate leaseStartDate;       // Kab se rehna hai
    private LocalDate leaseEndDate;         // Kab tak rehna hai
    private Integer leaseDurationMonths;    // Kitne mahine ka lease

    // Rent Details
    private Double monthlyRent;             // Monthly rent amount
    private Double securityDeposit;         // Security deposit
    private Double totalAmount;             // Security + First month rent

    // Booking Status
    @Enumerated(EnumType.STRING)
    private FlatBookingStatus status;       // PENDING, CONFIRMED, CANCELLED, COMPLETED

    // Payment
    private Boolean isPaid;                 // Payment hua ya nahi
    private String paymentId;              // Payment entity ka reference

    // Cancellation
    private String cancellationReason;      // Kyun cancel kiya
    private LocalDateTime cancelledAt;      // Kab cancel hua
    private String cancelledBy;            // Tenant ne ya Admin ne

    // Move In / Move Out
    private LocalDate actualMoveInDate;     // Actually kab aaya
    private LocalDate actualMoveOutDate;    // Actually kab gaya

}