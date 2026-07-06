package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "property_images")
public class PropertyImage extends BaseEnity{
    
    private String imageUrl;                
    private Boolean isCoverImage;     
    private Integer displayOrder;     

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

}