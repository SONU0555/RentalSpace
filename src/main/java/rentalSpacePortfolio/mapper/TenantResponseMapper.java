package rentalSpacePortfolio.mapper;

import java.util.ArrayList;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.entity.Tenant;



public class TenantResponseMapper {
    
    // Profile mapper
    public static ProfileResponse mapToProfileResponse(Tenant request){
        ProfileResponse response = new ProfileResponse();
        response.setFull_name(request.getUser().getFull_name());
        response.setEmail(request.getUser().getEmail());
        response.setPhone(request.getUser().getPhone());
        response.setEmergencyContect(request.getEmergencyContect());
        
        return response;
    }
    
    // Dashboard mapper
    public static DashboardResponse mapToDashboardResponse(Tenant request){
        DashboardResponse response = new DashboardResponse();
        response.setNumberOfproperties(10);
        response.setActiveBookings(5);
        response.setNumOfAmenityBookings(2);
        response.setNumOfMaintenanceRequests(4);
        response.setUpcommingBookings(new ArrayList<>());
        response.setMaintenanceStatus(new ArrayList<>());
        
        return response;
    }
   

}