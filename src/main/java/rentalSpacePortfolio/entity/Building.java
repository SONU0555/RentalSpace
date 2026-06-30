package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import rentalSpacePortfolio.enums.BuildingStatus;

@Entity
@Table(name = "buildings")
public class Building extends BaseEnity{            
    
    private String name;
    private Integer TotalFloors;
    
    @Enumerated(EnumType.STRING)
    private BuildingStatus status;
    
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

}