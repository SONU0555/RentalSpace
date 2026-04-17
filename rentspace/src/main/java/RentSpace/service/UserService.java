package RentSpace.service;

import RentSpace.dto.requestDto.UserLoginRequestDto;
import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.dto.responseDto.LoginUserResponseDto;
import RentSpace.dto.responseDto.UserResponseDto;
import RentSpace.entity.User;
import RentSpace.entityResponseMapper.UserResponseMapper;
import RentSpace.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    
    public UserRepository userRepo;
    
    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
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
        
        //Mapping Dto to Entity
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
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

}