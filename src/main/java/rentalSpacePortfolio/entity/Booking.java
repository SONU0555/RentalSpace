package rentalSpacePortfolio.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.BookingStatus;
import rentalSpacePortfolio.enums.BookingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bookings")
public class Booking extends BaseEnity{
    
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private AmenityBooking amenityBooking;

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private FlatBooking flatBooking;
    
    @Enumerated(EnumType.STRING)
    private BookingType bookingType;
    
    private Double totalAmount;       
    
    // Booking Status
    @Enumerated(EnumType.STRING)
    private BookingStatus status;       // PENDING, CONFIRMED, CANCELLED, COMPLETED

    // Payment
    private Boolean isPaid; 
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();            

    // Cancellation
    private String cancellationReason;      // Kyun cancel kiya
    private LocalDateTime cancelledAt;      // Kab cancel hua
    private String cancelledBy;            // Tenant ne ya Admin ne
    
    // booking Expiry
    private LocalDateTime holdExpireAt;

}