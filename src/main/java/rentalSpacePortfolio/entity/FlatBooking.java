package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flat_bookings")
public class FlatBooking extends BaseEnity{

    // Kya book kar raha hai
    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // Lease Details
    private LocalDate leaseStartDate;       // Kab se rehna hai
    private LocalDate leaseEndDate;         // Kab tak rehna hai
    private Integer leaseDurationMonths;    // Kitne mahine ka lease

    // Rent Details
    private Double monthlyRent;             // Monthly rent amount
    private Double securityDeposit;         // Security deposit
    
    // Move In / Move Out
    private LocalDate checkInDate;     // Actually kab aaya
    private LocalDate checkOutDate;    // Actually kab gaya

}