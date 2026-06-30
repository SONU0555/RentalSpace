package rentalSpacePortfolio.controller;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.service.PropertyService;


@RestController
@RequestMapping(ApiPaths.BASE + "/public/properties")
public class PublicPropertyController {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
    
    private final PropertyService propertyService;
    
    @Autowired
    public PublicPropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    // Endpoint to fetch all properties
    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperty(){
        
        logger.info("/properties endpoint hit to fetch all properties");
        
        List<PropertyResponse> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(ApiResponse.success("All properties fetched succesfully", properties));
    }
    
    // Endpoint to get property by id
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@RequestParam("id") String propertyId){
        PropertyResponse property = propertyService.getPropertyById(UUID.fromString(propertyId));
        return ResponseEntity.ok(ApiResponse.success("Property fetched successfully by Id", property));
    }

}