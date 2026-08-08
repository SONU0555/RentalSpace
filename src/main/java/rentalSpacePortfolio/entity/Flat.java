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
import java.util.List;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.FlatStatus;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flats")
public class Flat extends BaseEnity{
    
        private String buildingName;
        private String flatNumber;
        private Integer floorNumber;
        private String type;
        private Double areaSqFt;
        private Double rentAmount;
        private Double securityDeposit;
        
        @Enumerated(EnumType.STRING)
        private FlatStatus status;
        
//        private int step = 1;
//        private String tab = "flat";
        
        @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
        private List<FlatImage> flatImages = new ArrayList<>();
        
        @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
        private List<FlatBooking> flatBookingList = new ArrayList<>();
        
        @ManyToOne
        @JoinColumn(name = "property_id")
        private Property property;
        
        @OneToOne
        private Tenant currentTenant;

}