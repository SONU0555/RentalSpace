package RentSpace.service;

import RentSpace.dto.requestDto.UserDetailsRequestDto;
import RentSpace.dto.requestDto.UserLoginRequestDto;
import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.dto.responseDto.LoginUserResponseDto;
import RentSpace.dto.responseDto.UserResponseDto;
import RentSpace.entity.User;
import RentSpace.entityResponseMapper.UserResponseMapper;
import RentSpace.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    public UserRepository userRepo;
    
    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
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
    public void createNewUser(UserSignupReqDto request){
        User user = new User();
        
        System.out.println("user getting " + request.getName());
        
        //Mapping Dto to Entity
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder().encode(request.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        user.setRole(User.role.TENANT);
        userRepo.save(user);
    }
    
    // Find User by Id
    public UserResponseDto fetchUserById(Long id){
        User user = new User();
        if(user != null){
            return UserResponseMapper.mapToUserResponseDto(user);
        }
        
        throw new RuntimeException("User not found with Id: " + id);
    }
    
    // Login back existing user
    public String login(UserLoginRequestDto request){
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(!request.getPassword().equals(user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        
        return "LoggedIn success user: " + user.getName();
    }
    
    // Add user personal details
    public void addUserPeronalDetails(UserDetailsRequestDto request, Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with Id: " + id));
        
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAadhaarNumber(request.getAadhaarNumber());
        user.setRentStartDate(LocalDate.now());
        user.setRentEndDate(request.getRentEndDate());
        user.setUpdatedDate(LocalDateTime.now());
        user.setEmergencyContect(request.getEmergencyContect());
        user.setPropertyId(10L);
        user.setCompanyName("SKSpaceHub");
        
        userRepo.save(user);

    }
    
    // Delete user
    public void deleteUser(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with Id: " + id));
        userRepo.delete(user);
    }

}