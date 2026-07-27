package rentalSpacePortfolio.entity;

import jakarta.persistence.Embedded;
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
@Table(name = "amenity_images")
public class AmenityImage extends BaseEnity implements BelongingImage<Amenity>{
    
    @Embedded
    private ImageDetails imageDetails = new ImageDetails();
    
    @ManyToOne
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

    @Override
    public void setParent(Amenity parentEntity) {
        this.amenity = parentEntity;
    }
    
}