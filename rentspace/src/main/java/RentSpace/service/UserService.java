package RentSpace.service;

import RentSpace.requestDtos.User.UserProfileUpdateRequestDto;
import RentSpace.requestDtos.User.UserLoginRequestDto;
import RentSpace.requestDtos.User.UpdateCredentailsRequestDto;
import RentSpace.requestDtos.User.UserSignupReqDto;
import RentSpace.requestDtos.User.UserDetailsRequestDto;
import RentSpace.dto.responseDto.LoginUserResponseDto;
import RentSpace.dto.responseDto.UserResponseDto;
import RentSpace.entity.Property;
import RentSpace.entity.User;
import RentSpace.entityResponseMapper.UserResponseMapper;
import RentSpace.repository.UserRepository;
import RentSpace.requestDtos.User.OwnerDetailsRequestDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    public UserRepository userRepo;
    public PasswordEncoder PasswordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.PasswordEncoder = passwordEncoder;
    }
    
//     Password Encoder use BCrytp
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    // Fetch all users with complete Details
    public List<UserResponseDto> fetchAllUsers(){
        List<User> fetchAllUsers = userRepo.findAll();
        return fetchAllUsers.stream().map(user -> UserResponseMapper.mapToUserResponseDto(user)).collect(Collectors.toList());
    }
    
    // Fetch all LogedIn users tenant users
    public List<LoginUserResponseDto> fetchAllLogedInUsers(){
        List<User> fetchAllUsers = userRepo.findAll();
        return fetchAllUsers.stream().map(user -> UserResponseMapper.mapToLoginResponseDto(user)).collect(Collectors.toList());
    }
    
    
    // Register new user as tenant
    public void createNewUser(UserSignupReqDto request, String role){
        User user = new User();
                
        //Mapping Dto to Entity
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder().encode(request.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        user.setRole(role.equalsIgnoreCase("tenant") ? User.role.TENANT : User.role.OWNER);
        userRepo.save(user);
    }
    
    // Find User by Id
    public UserResponseDto fetchUserById(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with Id: " + id));
        
        return UserResponseMapper.mapToUserResponseDto(user);
    }
    
    // Find user by username
    public UserResponseDto fetchUserByUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        
        return fetchUserById(user.getId());
    }
    
    // Login back existing user
    public String login(UserLoginRequestDto request){
        User user = userRepo.findByEmail(request.getEmail());
        
        if(PasswordEncoder.matches(request.getPassword(), user.getPassword())){
            return  "LoggedIn success user: " + user.getName();
        }else{
            throw new RuntimeException("Invalid password");
        }
  
    }
    
    // Add user personal details
    public void addUserProfileDetails(UserDetailsRequestDto request, Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        
        if(user.getId().equals(id)){
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setRentStartDate(LocalDate.now());
            user.setRentEndDate(request.getRentEndDate());
            user.setAadhaarNumber(request.getAadhaarNumber());
            user.setUpdatedDate(LocalDateTime.now());
            user.setEmergencyContect(request.getEmergencyContect());

            userRepo.save(user);            
        }else{
            throw new RuntimeException("User not found with Id: " + id);
        } 

    }
    
    // Add Owner personal details
    public void addOwnerProfileDetails(OwnerDetailsRequestDto request, Long id){
        
        User owner = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wrong Id: " + id));
        
            owner.setPhone(request.getPhone());
            owner.setAddress(request.getAddress());
            owner.setAadhaarNumber(request.getAadhaarNumber());
            owner.setUpdatedDate(LocalDateTime.now());
            owner.setEmergencyContect(request.getEmergencyContect());
            owner.setCompanyName(request.getCompanyName());

            userRepo.save(owner);            

    }    
    
    // Update user profile details
    public void updateProfile(UserProfileUpdateRequestDto request, Long id){
        User owner = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wrong owner Id: " + id));
        
            owner.setName(request.getName());
            owner.setEmail(request.getEmail());
            owner.setPhone(request.getPhone());
            owner.setAddress(request.getAddress());
            owner.setAadhaarNumber(request.getAadhaarNumber());
            owner.setEmergencyContect(request.getEmergencyContect()); 
            owner.setCompanyName(request.getCompanyName());
            owner.setUpdatedDate(LocalDateTime.now());
                 
            userRepo.save(owner);

    }
    
    // Update Credentails of the user
    public void updateCredential(UpdateCredentailsRequestDto request, Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        
        if(user.getId().equals(id)){
            if(PasswordEncoder.matches(request.getOldPassword(), user.getPassword())){
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder().encode(request.getNewPassword()));
                userRepo.save(user);
            }else{
                throw new RuntimeException("Old password missmatch");
            }
        } else{
            throw new RuntimeException("User not found with Id: " + id);
        }
        
        
    }
    
    // Delete user
    public void deleteUser(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with Id: " + id));
        userRepo.delete(user);
    }

}