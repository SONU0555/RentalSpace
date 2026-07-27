package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.AmenityStatus;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "buildings")
public class Building extends BaseEnity{            
    
    private String name;
    private Integer TotalFloors;
    
    @Enumerated(EnumType.STRING)
    private AmenityStatus status;
    
//    @ManyToOne
//    @JoinColumn(name = "property_id")
//    private Property property;

}