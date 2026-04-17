package RentSpace.controller.Public;

import RentSpace.dto.requestDto.UserLoginRequestDto;
import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/user")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
    
    @GetMapping("/all/logedin")
    public ResponseEntity<?> getAllLogedinUsers(){
        return new ResponseEntity<>(userService.fetchAllLogedInUsers(), HttpStatus.OK);
    }
    
    
    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody UserSignupReqDto request){
        try{
            userService.createNewUser(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/find")
    public ResponseEntity<?> findUserById(@RequestParam Long id){
        try{
            return ResponseEntity.ok(userService.fetchUserById(id));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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