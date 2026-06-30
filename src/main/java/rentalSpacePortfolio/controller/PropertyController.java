package rentalSpacePortfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.property.AddRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.exception.BadRequestException;
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
    public ResponseEntity<String> addProperty(@Valid @RequestBody AddRequest request){
        try{
            
            logger.info("Requested to add property");
            propertyService.addProperty(request);
            logger.info("All data are valid to create new property");
            
            return new ResponseEntity<>("Property added successfully", HttpStatus.CREATED); 
        }catch(BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
    }
    
    
}