package RentSpace.controller.Private;

import RentSpace.requestDtos.User.OwnerDetailsRequestDto;
import RentSpace.requestDtos.User.OwnerProfileUpdateDto;
import RentSpace.requestDtos.User.UserDetailsRequestDto;
import RentSpace.requestDtos.User.UserProfileUpdateRequestDto;
import RentSpace.responseDto.UserResponseDto;
import RentSpace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/private/owner")
public class OwnerPrivateController {
    
    private final UserService userService;
    
    @Autowired
    public OwnerPrivateController(UserService userService){
        this.userService = userService;
    }
    
    @GetMapping("/view")
    public ResponseEntity<?> viewOwnerProfile(@RequestParam Long id){
        return new ResponseEntity<>(userService.fetchUserById(id), HttpStatus.OK);
    }
    
    @PostMapping("/create-profile")
    public ResponseEntity<?> createProfile(@Valid @RequestBody OwnerDetailsRequestDto request, @RequestParam Long id){
            userService.addOwnerProfileDetails(request, id);
            return new ResponseEntity<>("Successfully created", HttpStatus.OK);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody OwnerProfileUpdateDto request, @RequestParam Long id){
             userService.updateOwnerProfile(request, id);
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
//    @GetMapping("/view-tenants")
//    public ResponseEntity<UserResponseDto> viewAllTenants()

}