package RentSpace.controller.Private;

import RentSpace.dto.requestDto.UpdateCredentailsRequestDto;
import RentSpace.dto.requestDto.UserDetailsRequestDto;
import RentSpace.dto.requestDto.UserSignupReqDto;
import RentSpace.repository.UserRepository;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
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
@RequestMapping("/private/user")
public class UserPrivateController {
    
    private final UserService userService;
    private final UserRepository userRepo;
    
    @Autowired
    public UserPrivateController(UserService userService, UserRepository userRepo){
        this.userService = userService;
        this.userRepo = userRepo;
    }
    
    // Updat users personal details
    @PutMapping("/add/personaldetails")
    public ResponseEntity<?> updateUserPersonalDetails(@Valid @RequestBody UserDetailsRequestDto request, @RequestParam Long id){
        try{
            userService.addUserPeronalDetails(request, id);
            return new ResponseEntity<>("Successfully added", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // Update users credentials only username(email) and password
    @PutMapping("/update")
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