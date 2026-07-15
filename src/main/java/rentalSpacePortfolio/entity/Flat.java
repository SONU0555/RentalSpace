package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    
        private String flatNumber;
        private Integer floorNumber;
        private String type;
        private Double areaSqFt;
        private Double rentAmount;
        
        @Enumerated(EnumType.STRING)
        private FlatStatus status;
        
        @ManyToOne
        @JoinColumn(name = "building_id")
        private Building building;
        
        @OneToOne
        private Tenant currentTenant;

}