package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.auth.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.auth.AuthService;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.service.AdminService;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final AuthService authService;
    private final AdminService adminService;
//    private final PaymentService paymentService;
    private final UserRepository userRepo;
    
    @Autowired
    public AdminController(AuthService signupAndLoginService,
            UserRepository userRepo,
//            PaymentService paymentService,
            AdminService adminService){
        this.authService = signupAndLoginService;
        this.userRepo = userRepo;
//        this.paymentService = paymentService;
        this.adminService = adminService;
    }
    
    @PostMapping("/owner/create")
    public ResponseEntity<?> createOwner(@Valid @RequestBody RegisterRequest request){
        try{
            authService.createNewUser(request, "owner");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
            adminService.deleteUser(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
    
    
    // Get all property payments done by tenants
//    @GetMapping("/view-payments/{id}")
//    public ResponseEntity<List<PaymentResponseDto>> getAllPropertyRentPaymentHistory(@PathVariable Long id){
//        return new ResponseEntity<>(paymentService.fetchAllPaymentHistory(id), HttpStatus.OK);
//    }
    
}