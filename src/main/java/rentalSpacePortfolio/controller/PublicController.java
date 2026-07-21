package rentalSpacePortfolio.controller;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.service.impl.FlatService;
import rentalSpacePortfolio.service.impl.PropertyService;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE + "/public")
public class PublicController {
        
    private final PropertyService propertyService;
    private final FlatService flatService;
    
    @Autowired
    public PublicController(PropertyService propertyService, FlatService flatService){
        this.propertyService = propertyService;
        this.flatService = flatService;
    }
    
    // Endpoint to fetch all properties
    @GetMapping("/properties")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperty(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size){
        
        log.info("/properties endpoint hit to fetch all properties");
        
        List<PropertyResponse> properties = propertyService.getAllProperties(page, size);
        return ResponseEntity.ok(ApiResponse.success("All properties fetched succesfully", properties));
    }
    
    // Endpoint to get property by id
    @GetMapping("/properties/{id}")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@PathVariable("id") String propertyId){
       
        log.info("Received get property request. PropertyId: {}", propertyId);
       
        PropertyResponse property = propertyService.getPropertyById(UUID.fromString(propertyId));
        return ResponseEntity.ok(ApiResponse.success("Property fetched successfully by Id", property));
    }

}