package rentalSpacePortfolio.controller;

import rentalSpacePortfolio.dto.request.tenant.ProfileRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.tenant.AadhaarRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.impl.TenantService;
import rentalSpacePortfolio.service.impl.CommonService;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE + "/tenants")
public class TenantController {
    
    private final TenantService tenantService;
    private final CommonService userCommonService;
    
    @Autowired
    public TenantController(TenantService tenantService,
            CommonService userCommonService){
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
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(){
        
            String userId = SecurityUnits.getCurrentUserId();
        
            ProfileResponse profile = tenantService.getTenantProfile(UUID.fromString(userId));
            return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    // endpoint to get all tenant
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @GetMapping("/")
    public ResponseEntity<List<TenantSummaryResponse>> getAllTenants(){
        
        log.info("Received request to fetch all tenants");
        String userId = SecurityUnits.getCurrentUserId();
        
        return new ResponseEntity<>(tenantService.getAllTenants(UUID.fromString(userId)), HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @GetMapping("/{tenantId}")
    public ResponseEntity<ApiResponse<TenantSummaryResponse>> getTenantById(@PathVariable UUID tenantId){
        TenantSummaryResponse tenant = userCommonService.getTenantById(tenantId);
        return ResponseEntity.ok(ApiResponse.success("Admin fetched by Id successfully", tenant));
    }
    
    
    // Update users profile personal details
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody ProfileRequest request){
        
            String userId = SecurityUnits.getCurrentUserId();
        
             tenantService.updateTenantProfile(request, UUID.fromString(userId));
             return new ResponseEntity<>("Profile successfully updated", HttpStatus.ACCEPTED);
    }
    
    // To varify aadhaar card 
    @PostMapping("/profile/verification")
    public ResponseEntity<String> aadhaarVerification(@Valid @RequestBody AadhaarRequest request){
        
            String userId = SecurityUnits.getCurrentUserId();
        
            tenantService.aadhaarVerification(request, UUID.fromString(userId));
            return new ResponseEntity<>("Varification successfully completed", HttpStatus.ACCEPTED);
    }
    
    
}