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
@Table(name = "Flat_image")
public class FlatImage extends BaseEnity implements BelongingImage<Flat>{

    @Embedded
    private ImageDetails imageDetails = new ImageDetails();

    @ManyToOne
    @JoinColumn(name = "Flat_id")
    private Flat flat;

    @Override
    public void setParent(Flat parentEntity) {
        this.flat = flat;
    }

}