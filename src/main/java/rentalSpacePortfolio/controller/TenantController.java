package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.tenant.ProfileUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.TenantService;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {
    
    private final TenantService tenantService;
    
    @Autowired
    public TenantController(TenantService tenantService){
        this.tenantService = tenantService;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(){
            String userId = SecurityUnits.getCurrentUserId();
            
            DashboardResponse dashboard = tenantService.getTenantDashboard(userId);
            return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/profile") // View user profile
    public ResponseEntity<ProfileResponse> getProfile(){
        
            String userId = SecurityUnits.getCurrentUserId();
        
            ProfileResponse profile = tenantService.getTenantProfile(userId);
            return new ResponseEntity<>(profile, HttpStatus.OK);
    }
    
    
    // Update users profile details
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request){
        
            String userId = SecurityUnits.getCurrentUserId();
        
             tenantService.updateTenantProfile(request, userId);
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
}