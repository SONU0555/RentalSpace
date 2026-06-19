package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.owner.DetailsRequest;
import rentalSpacePortfolio.dto.request.owner.ProfileUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.dto.request.auth.RegisterRequest;
import rentalSpacePortfolio.auth.AuthService;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.OwnerService;
import rentalSpacePortfolio.service.UserCommonService;


@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    
    private final OwnerService ownerService;
    private final AuthService authService;
    private final UserCommonService userCommonService;
    
    @Autowired
    public OwnerController(OwnerService ownerService,
            AuthService authService,
            UserCommonService userCommonService){
        this.ownerService = ownerService;
        this.authService  = authService;
        this.userCommonService = userCommonService;
    }
    
    
    @PostMapping("/register/admin") // endpoint to create new admin
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegisterRequest request){
        try{
            authService.createNewUser(request, "ADMIN");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/admins") // endpoint to get all admins
    public ResponseEntity<List<AdminSummaryResponse>> getAllAdmins(){
        return new ResponseEntity<>(ownerService.getAllAdmins(), HttpStatus.OK);
    }
    
    @GetMapping("/admins/{adminId}")
    public ResponseEntity<AdminSummaryResponse> getAdminById(@PathVariable String adminId){
        return new ResponseEntity<>(userCommonService.getAdminById(adminId), HttpStatus.OK);
    }
    
    @GetMapping("/tenants") // endpoint to get all admins
    public ResponseEntity<List<TenantSummaryResponse>> getAllTenants(){
        return new ResponseEntity<>(ownerService.getAllTenants(), HttpStatus.OK);
    }
    
    @GetMapping("/tenants/{tenantId}")
    public ResponseEntity<TenantSummaryResponse> getTenantById(@PathVariable String tenantId){
        return new ResponseEntity<>(userCommonService.getTenantById(tenantId), HttpStatus.OK);
    }
//    
//    @PostMapping("/create-profile")
//    public ResponseEntity<?> createProfile(@Valid @RequestBody DetailsRequest request, @RequestParam Long id){
//            ownerService.addOwnerProfileDetails(request, id);
//            return new ResponseEntity<>("Successfully created", HttpStatus.OK);
//    }
//    
//    @PutMapping("/update")
//    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request, @RequestParam Long id){
//             ownerService.updateOwnerProfile(request, id);
//             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
//    }
    
//    @GetMapping("/view-tenants")
//    public ResponseEntity<UserResponseDto> viewAllTenants()

}