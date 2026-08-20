package rentalSpacePortfolio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetails {
    
    private String imageUrl;                
    private Boolean isCoverImage;     
    private Integer displayOrder; 

}