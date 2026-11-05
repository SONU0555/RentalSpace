package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "buildings")
public class Building extends BaseEnity{            
    
    private String name;
    private Integer TotalFloors;
    private char buildingCode;
    
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
    
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Flat> flats = new ArrayList<>();

}