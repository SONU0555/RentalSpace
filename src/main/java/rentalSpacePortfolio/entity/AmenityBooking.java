package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
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
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // Slot Details
    private LocalDate bookingDate;          // Kaunse din
    private LocalTime startTime;            // Slot start time
    private LocalTime endTime;              // Slot end time
    private Integer durationMinutes;        // Kitne minutes ka slot

}