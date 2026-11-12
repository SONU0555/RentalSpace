package rentalSpacePortfolio.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImageDetails {
    
    private String imageUrl;                
    private Boolean isCoverImage;     
    private Integer displayOrder; 
 
}