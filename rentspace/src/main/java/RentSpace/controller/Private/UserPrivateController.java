package RentSpace.controller.Private;

import RentSpace.requestDtos.User.UpdateCredentailsRequestDto;
import RentSpace.requestDtos.User.UserDetailsRequestDto;
import RentSpace.requestDtos.User.UserProfileUpdateRequestDto;
import RentSpace.responseDto.PropertyResponseDto;
import RentSpace.service.PropertyService;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/user")
public class UserPrivateController {
    
    private final UserService userService;
    private final PropertyService propertyService;
    
    @Autowired
    public UserPrivateController(UserService userService, PropertyService propertyService){
        this.userService = userService;
        this.propertyService = propertyService;
    }
    
    // add users personal details
    @PutMapping("/add/personal-details")
    public ResponseEntity<?> addUserPersonalDetails(@Valid @RequestBody UserDetailsRequestDto request, @RequestParam Long id){
            userService.addUserProfileDetails(request, id);
            return new ResponseEntity<>("Successfully added", HttpStatus.OK);
    }
    
    // Update users profile details
    @PutMapping("/update/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequestDto request, @RequestParam Long id){
             userService.updateUserProfile(request, id);
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
    // Update users credentials only username(email) and password
    @PutMapping("/update/credentials")
    public ResponseEntity<?> updateUserCredential(@RequestBody UpdateCredentailsRequestDto request, @RequestParam Long id){
            userService.updateCredential(request, id);
            return ResponseEntity.ok("Successfully updated");   
    }    
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByUserName(@RequestParam Long id){
            userService.deleteUser(id);
            return new ResponseEntity<>("Deleted added", HttpStatus.OK);     
    }  
}