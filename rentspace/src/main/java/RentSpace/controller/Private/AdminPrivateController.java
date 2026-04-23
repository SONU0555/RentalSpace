package RentSpace.controller.Private;

import RentSpace.requestDtos.User.UserSignupReqDto;
import RentSpace.repository.UserRepository;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @PostMapping("/owner/create")
    public ResponseEntity<?> createOwner(@Valid @RequestBody UserSignupReqDto request){
        try{
            userService.createNewUser(request, "owner");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/view-all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
    
    @GetMapping("/user/view-all-loggedin")
    public ResponseEntity<?> getAllLogedinUsers(){
        return new ResponseEntity<>(userService.fetchAllLogedInUsers(), HttpStatus.OK);
    }    
    
    @GetMapping("/user/find")
    public ResponseEntity<?> findUserById(@RequestParam Long id){
            return ResponseEntity.ok(userService.fetchUserById(id));
    }    
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
            userService.deleteUser(id);
            return new ResponseEntity<>("Deleted added", HttpStatus.OK);
    }

}