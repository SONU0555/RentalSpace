package rentalSpacePortfolio.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.auth.AuthService;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.user.ProfileResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.OwnerService;
import rentalSpacePortfolio.service.UserCommonService;


@RestController
@RequestMapping(ApiPaths.BASE + "/owner")
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
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(){
        String userId = SecurityUnits.getCurrentUserId();
        
        ProfileResponse profile = ownerService.getOwnerProfile(UUID.fromString(userId));
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
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