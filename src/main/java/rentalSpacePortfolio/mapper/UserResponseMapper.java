package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.user.ProfileResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;


public class UserResponseMapper {
    
//    public static LoginUserResponseDto mapToLoginResponseDto(User user){
//        LoginUserResponseDto response = new LoginUserResponseDto();
//        response.setId(user.getId());
//        response.setName(user.getFull_name());
//        response.setEmail(user.getEmail());
//        response.setCreatedDate(user.getCreatedDate());
//        response.setUpdatedDate(user.getUpdatedDate());
//        
//        return response;
//    }
    
        public static TenantSummaryResponse mapToTenatResponseDto(Tenant tenant){
        TenantSummaryResponse response = new TenantSummaryResponse();
        response.setId(tenant.getId().toString());
        response.setFull_name(tenant.getUser().getFull_name());
        response.setEmail(tenant.getUser().getEmail());
        response.setPhone(tenant.getUser().getPhone());
        response.setEmergencyContect(tenant.getEmergencyContect());
        response.setCreatedDate(tenant.getCreatedAt());
        response.setUpdatedDate(tenant.getUpdatedAt());
        
        return response;
    }
        
        
//     map user Entity to AdminSummaryDto
    public static AdminSummaryResponse mapUserToAdminSummaryDto(Admin admin){
        AdminSummaryResponse response = new AdminSummaryResponse();
        response.setId(admin.getId().toString());
        response.setFull_name(admin.getUser().getFull_name());
        response.setEmail(admin.getUser().getEmail());
        response.setPhone(admin.getUser().getPhone());
//        response.setProperty_assigned(admin.getProperty().getId());
        response.setCreatedDate(admin.getUser().getCreatedAt());
        response.setUpdatedDate(admin.getUser().getUpdatedAt());
        
        return response;
    }
    
//     map user Entity to AdminSummaryDto
    public static AdminSummaryResponse mapUserToAdminSummaryDtoForTenant(Admin admin){
        AdminSummaryResponse response = new AdminSummaryResponse();
        response.setEmployeeCode(admin.getEmployeeCode());
        response.setFull_name(admin.getUser().getFull_name());
        response.setEmail(admin.getUser().getEmail());
        response.setPhone(admin.getUser().getPhone());
        
        return response;
    }
    
    // map to owner profile response
    public static ProfileResponse mapToOwnerProfile(User owner){
        ProfileResponse response = new ProfileResponse();
        response.setFullName(owner.getFull_name());
        response.setEmail(owner.getEmail());
        response.setRole(owner.getRole().toString());
        return response;
    }

}