package rentalSpacePortfolio.dto.image;

import jakarta.persistence.Embedded;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.entity.ImageDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    
    private UUID id;
    @Embedded
    private ImageDetails imageDetails;

}