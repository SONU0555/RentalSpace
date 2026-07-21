package rentalSpacePortfolio.controller;


import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.auth.AuthService;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.admin.ProfileResponse;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.impl.AdminService;


@RestController
@RequestMapping(ApiPaths.BASE + "/admins")
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
    
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(){
        
            String userId = SecurityUnits.getCurrentUserId();
            
            ProfileResponse profile = adminService.getAdminProfile(UUID.fromString(userId));
            return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }
    
    @GetMapping // endpoint to get all admins
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<AdminSummaryResponse>> getAllAdmins(){
        return new ResponseEntity<>(adminService.getAllAdmins(), HttpStatus.OK);
    }
    
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminSummaryResponse>> getAdminById(@PathVariable UUID adminId){
        
        AdminSummaryResponse admin = adminService.getAdminById(adminId);
        
        return ResponseEntity.ok(ApiResponse.success("Admin fetched by Id successfully", admin));
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam UUID id){
            adminService.deleteUser(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
    
    
    // Get all property payments done by tenants
//    @GetMapping("/view-payments/{id}")
//    public ResponseEntity<List<PaymentResponseDto>> getAllPropertyRentPaymentHistory(@PathVariable Long id){
//        return new ResponseEntity<>(paymentService.fetchAllPaymentHistory(id), HttpStatus.OK);
//    }
    
}