package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "amenity_bookings")
public class AmenityBooking extends BaseEnity{

    // Kaunsi amenity book kar raha hai
    @ManyToOne
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    private boolean dayBookingAlertSent = false;
    private boolean hourBeforeAlertSent = false;

    // Slot Details
    private LocalDateTime startAt;
    private LocalDateTime endAt;

}