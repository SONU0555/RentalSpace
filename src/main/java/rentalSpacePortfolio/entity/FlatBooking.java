package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

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
    
    // Move In / Move Out
    private LocalDate checkInDate;     // Actually kab aaya
    private LocalDate checkOutDate;    // Actually kab gaya

}