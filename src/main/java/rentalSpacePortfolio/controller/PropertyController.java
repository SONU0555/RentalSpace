package rentalSpacePortfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.property.AddRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.exception.BadRequestException;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.PropertyService;


@RestController
@RequestMapping(ApiPaths.BASE + "/properties")
public class PropertyController {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
    
    private final PropertyService propertyService;
    
    @Autowired
    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    // Endpoint to add new property
    @PostMapping("/add")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> addProperty(@Valid @RequestBody AddRequest request){      
        
            logger.info("Requested to add property");
            String ownerId = SecurityUnits.getCurrentUserId();
            propertyService.addProperty(request, UUID.fromString(ownerId));
            logger.info("All data are valid to create new property");
            
            return new ResponseEntity<>((new ApiResponse<>(true, "Property added successfully!", "Empty")), HttpStatus.CREATED); 
    }
    
    // Endpoint to assign admin to a specific property
    @PostMapping("/{propertyId}/assign")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> assingAdminToProperty(@PathVariable("propertyId") UUID propertyId,
            @RequestParam("admin") UUID adminId){
        
        String ownerId = SecurityUnits.getCurrentUserId();
        logger.info("Received request to assign adminId: {} to propertyId: {} by ownerId: {}", adminId, propertyId, ownerId);
        propertyService.assignPropertyToAdmin(propertyId, adminId, UUID.fromString(ownerId));
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin assigned successfully!", "Empty"));
    }
    
}