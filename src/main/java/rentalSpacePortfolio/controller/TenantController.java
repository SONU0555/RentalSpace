package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.tenant.ProfileUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rentalSpacePortfolio.dto.request.tenant.ChangePasswordRequest;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
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

    @GetMapping // endpoint to get all admins
    @PreAuthorize("hasRole('ADMIN','OWNER')")
    public ResponseEntity<List<TenantSummaryResponse>> getAllTenants(){
        
        String userId = SecurityUnits.getCurrentUserId();
        
        return new ResponseEntity<>(tenantService.getAllTenants(UUID.fromString(userId)), HttpStatus.OK);
    }
    
    @GetMapping("/{tenantId}")
    @PreAuthorize("hasRole('ADMIN','OWNER')")
    public ResponseEntity<TenantSummaryResponse> getTenantById(@PathVariable UUID tenantId){
        return new ResponseEntity<>(userCommonService.getTenantById(tenantId), HttpStatus.OK);
    }
    
    
    // Update users profile details
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request){
        
            String userId = SecurityUnits.getCurrentUserId();
        
             tenantService.updateTenantProfile(request, UUID.fromString(userId));
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
}