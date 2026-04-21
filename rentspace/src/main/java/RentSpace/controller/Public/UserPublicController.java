package RentSpace.controller.Public;

import RentSpace.dto.requestDto.UserLoginRequestDto;
import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/user")
public class UserPublicController {
    
    private final UserService userService;
    
    @Autowired
    public UserPublicController(UserService userService){
        this.userService = userService;
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody UserSignupReqDto request){
        try{
            userService.createNewUser(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto request){
        try{
            String success = userService.login(request);
            return ResponseEntity.ok(success);
        }catch (Exception e){
            return ResponseEntity.ok("FAILED: " + e.getMessage());
        }
    }

}