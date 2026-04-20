package RentSpace.controller.Private;

import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.repository.UserRepository;
import RentSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/private/admin")
public class AdminPrivateController {
    
    private final UserService userService;
    private final UserRepository userRepo;
    
    @Autowired
    public AdminPrivateController(UserService userService, UserRepository userRepo){
        this.userService = userService;
        this.userRepo = userRepo;
    }
    
    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
    
    @GetMapping("/user/all-loggedin")
    public ResponseEntity<?> getAllLogedinUsers(){
        return new ResponseEntity<>(userService.fetchAllLogedInUsers(), HttpStatus.OK);
    }    
    
    @GetMapping("/user/find")
    public ResponseEntity<?> findUserById(@RequestParam Long id){
        try{
            return ResponseEntity.ok(userService.fetchUserById(id));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        try{
            userService.deleteUser(id);
            return new ResponseEntity<>("Deleted added", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }        
    }

}