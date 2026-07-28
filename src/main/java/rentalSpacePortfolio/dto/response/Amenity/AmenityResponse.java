package rentalSpacePortfolio.dto.response.Amenity;


import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.dto.image.ImageResponse;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.enums.AmenityStatus;
import rentalSpacePortfolio.enums.AmenityType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityResponse {
    
    private UUID id;
    private String name;                  
    private String description;            

    private AmenityType type;               
    private AmenityStatus status;          

    // Capacity & Timing
    private Integer maxCapacity;            
    private LocalTime openTime;            
    private LocalTime closeTime;            
    private Integer slotDurationMinutes;   

    // Charges
    private Boolean isPaid;               
    private Double chargePerSlot;         

    // Rules
    private Integer maxSlotsPerTenant;      
    private Integer advanceBookingDays;     
    private Property property;
    private List<ImageResponse> amenityImages;

}