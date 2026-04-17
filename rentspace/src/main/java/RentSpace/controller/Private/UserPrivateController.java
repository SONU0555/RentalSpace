package RentSpace.controller.Private;

import RentSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/user")
public class UserPrivateController {
    
    private UserService userService;
    
    @Autowired
    public UserPrivateController(UserService userService){
        this.userService = userService;
    }

}