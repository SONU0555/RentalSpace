package rentalSpacePortfolio.auth;

import rentalSpacePortfolio.dto.request.auth.LoginRequest;
import rentalSpacePortfolio.dto.request.auth.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.exception.BadRequestException;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegisterRequest request){
        try{
            authService.createNewUser(request, "TENANT");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try{
            String success = authService.login(request);
            return ResponseEntity.ok(success);
        }catch (Exception e){
            return ResponseEntity.ok("FAILED: " + e.getMessage());
        }
    }

}