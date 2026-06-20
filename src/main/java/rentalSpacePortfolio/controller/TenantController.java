package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.tenant.ProfileUpdateRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rentalSpacePortfolio.dto.request.tenant.ChangePasswordRequest;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.exception.BadRequestException;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.TenantService;
import rentalSpacePortfolio.service.UserCommonService;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    
    private final TenantService tenantService;
    private final UserCommonService userCommonService;
    
    @Autowired
    public TenantController(TenantService tenantService,
            UserCommonService userCommonService){
        this.tenantService = tenantService;
        this.userCommonService = userCommonService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(){
            String userId = SecurityUnits.getCurrentUserId();
            
            DashboardResponse dashboard = tenantService.getTenantDashboard((UUID.fromString(userId)));
            return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/profile") // View user profile
    public ResponseEntity<ProfileResponse> getProfile(){
        
            String userId = SecurityUnits.getCurrentUserId();
        
            ProfileResponse profile = tenantService.getTenantProfile(UUID.fromString(userId));
            return new ResponseEntity<>(profile, HttpStatus.OK);
    }
    
    
    // Update users profile details
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request){
        
            String userId = SecurityUnits.getCurrentUserId();
        
             tenantService.updateTenantProfile(request, UUID.fromString(userId));
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
    // Change user password
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        
        String userId = SecurityUnits.getCurrentUserId();
        try{
            userCommonService.changeTenantPassword(request, UUID.fromString(userId));
            return ResponseEntity.ok("Change password successfull");
        }catch(Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
    
}