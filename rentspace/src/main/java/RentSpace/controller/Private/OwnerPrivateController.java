package RentSpace.controller.Private;

import RentSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> viewOwnerProfile(){
        return new ResponseEntity<>(userService.fetchUserByUserName(), HttpStatus.OK);
    }
    

}