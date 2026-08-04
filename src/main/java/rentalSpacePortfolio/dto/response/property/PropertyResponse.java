package rentalSpacePortfolio.dto.response.property;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.PropertyTier;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    
    private String id;
    private String name;
    private String description;
    private String city;
    private String state;
    private String status;
    private PropertyTier propertyTier;
    private List<ImageResponse> images;
    private Double miniumRent;
    private Double maximumRent;

}