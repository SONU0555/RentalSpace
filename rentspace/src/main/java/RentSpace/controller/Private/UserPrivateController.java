package RentSpace.controller.Private;

import RentSpace.requestDtos.User.UpdateCredentailsRequestDto;
import RentSpace.requestDtos.User.UserDetailsRequestDto;
import RentSpace.requestDtos.User.UserProfileUpdateRequestDto;
import RentSpace.repository.UserRepository;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/user")
public class UserPrivateController {
    
    private final UserService userService;
    private final UserRepository userRepo;
    
    @Autowired
    public UserPrivateController(UserService userService, UserRepository userRepo){
        this.userService = userService;
        this.userRepo = userRepo;
    }
    
    // add users personal details
    @PutMapping("/add/personal-details")
    public ResponseEntity<?> addUserPersonalDetails(@Valid @RequestBody UserDetailsRequestDto request, @RequestParam Long id){
        try{
            userService.addUserProfileDetails(request, id);
            return new ResponseEntity<>("Successfully added", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // Update users profile details
    @PutMapping("/update/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequestDto request, @RequestParam Long id){
        try{
             userService.updateProfile(request, id);
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // Update users credentials only username(email) and password
    @PutMapping("/update/credentials")
    public ResponseEntity<?> updateUserCredential(@RequestBody UpdateCredentailsRequestDto request, @RequestParam Long id){
        
        try{
            userService.updateCredential(request, id);
            return ResponseEntity.ok("Successfully updated");
        }catch(Exception e){
            return new ResponseEntity<>("ERROR " +  e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }    
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByUserName(@RequestParam Long id){
        try{
            userService.deleteUser(id);
            return new ResponseEntity<>("Deleted added", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }        
    }

}