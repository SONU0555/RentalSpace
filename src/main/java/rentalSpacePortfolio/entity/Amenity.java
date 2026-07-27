package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.AmenityStatus;
import rentalSpacePortfolio.enums.AmenityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "amenities")
public class Amenity extends BaseEnity {

    private String name;                    // "Swimming Pool", "Gym"
    private String description;             // Detail description

    @Enumerated(EnumType.STRING)
    private AmenityType type;               // SWIMMING_POOL, GYM, PARKING etc

    @Enumerated(EnumType.STRING)
    private AmenityStatus status;           // ACTIVE, INACTIVE, UNDER_MAINTENANCE

    // Capacity & Timing
    private Integer maxCapacity;            // Ek time pe kitne log use kar sakte hain
    private LocalTime openTime;             // "06:00 AM"
    private LocalTime closeTime;            // "10:00 PM"
    private Integer slotDurationMinutes;    // Ek booking kitne minutes ki hogi (60, 90, 120)

    // Charges
    private Boolean isPaid;                 // Free hai ya paid
    private Double chargePerSlot;           // Agar paid ho toh kitna

    // Rules
    private Integer maxSlotsPerTenant;      // Ek tenant ek din mein kitne slots book kar sakta hai
    private Integer advanceBookingDays;     // Kitne din pehle book kar sakte hain

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    // Images
    @OneToMany(mappedBy = "amenity", cascade = CascadeType.ALL)
    private List<AmenityImage> images = new ArrayList<>();
}