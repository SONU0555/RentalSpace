package RentSpace.entityResponseMapper;

import RentSpace.dto.responseDto.LoginUserResponseDto;
import RentSpace.dto.responseDto.UserResponseDto;
import RentSpace.entity.User;


public class UserResponseMapper {
    
    public static LoginUserResponseDto mapToLoginResponseDto(User user){
        LoginUserResponseDto response = new LoginUserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        
        return response;
    }
    
        public static UserResponseDto mapToUserResponseDto(User user){
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setAadhaarNumber(user.getAadhaarNumber());
        response.setEmergencyContect(user.getEmergencyContect());
        response.setProperties(user.getProperties());
        response.setRentStartDate(user.getRentStartDate());
        response.setRentEndDate(user.getRentEndDate());
        response.setCompanyName(user.getCompanyName());
        response.setRole(user.getRole());
        response.setCreatedDate(user.getCreatedDate());
        response.setUpdatedDate(user.getUpdatedDate());
        
        return response;
    }

}