package rentalSpacePortfolio.dto.response.tenant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    
    private Integer numberOfproperties;
    private Integer activeBookings;
    private Integer numOfAmenityBookings;
    private Integer numOfMaintenanceRequests;
    private List<?> upcommingBookings;
    private List<?> maintenanceStatus;
    
}