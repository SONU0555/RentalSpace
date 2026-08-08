package rentalSpacePortfolio.dto.response.flat;


import rentalSpacePortfolio.dto.image.ImageResponse;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.FlatStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlatResponse {
    
        private UUID id;
        private String buildingName;
        private String flatNumber;
        private Integer floorNumber;
        private String type;
        private Double areaSqFt;
        private Double rentAmount;
        private Double securityDeposit;
        private List<ImageResponse> flatImages;
        private FlatStatus status;

}