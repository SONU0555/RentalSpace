package rentalSpacePortfolio.mapper;

import rentalSpacePortfolio.dto.response.admin.ProfileResponse;
import rentalSpacePortfolio.entity.User;




public class AdminResponseMapper {
    
    public static ProfileResponse mapToProfileResponse(User request){
        ProfileResponse response = new ProfileResponse();
        response.setEmployeeCode(request.getAdmin().getEmployeeCode());
        response.setFullName(request.getFull_name());
        response.setEmail(request.getEmail());
        response.setPhone(request.getPhone());
        response.setLocaton(request.getAdmin().getLocation());
        return response;
    }

}