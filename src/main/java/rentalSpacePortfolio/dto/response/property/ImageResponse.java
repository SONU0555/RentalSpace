package rentalSpacePortfolio.dto.response.property;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    
    private UUID id;
    private String imageUrl; 
    private Integer displayOrder;
    private Boolean isCoverImage;
}