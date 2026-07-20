package rentalSpacePortfolio.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetails {
    
    private String imageUrl;                
    private Boolean isCoverImage;     
    private Integer displayOrder; 

}