package RentSpace.controller.Private;

import RentSpace.requestDtos.Property.AddPropertyDto;
import RentSpace.responseDto.PropertyResponseDto;
import RentSpace.service.PropertyService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class PropertyController {
    
    public static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
    
    private final PropertyService propertyService;
    
    @Autowired
    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addProperty(@Valid @RequestBody AddPropertyDto request, @RequestParam Long id){
        logger.info("User requested to add new property with owner Id: {}", id);
            propertyService.addNewProperty(request, id);
            return new ResponseEntity<>("Property added successfully", HttpStatus.OK);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateProperty(@Valid @RequestBody AddPropertyDto request, 
            @RequestParam Long propertyId, 
            @RequestParam Long ownerId){
        logger.info("Owner request with Id: {} to update property with Id: {}", ownerId, propertyId);
        propertyService.updateProperty(request, propertyId, ownerId);
        return new ResponseEntity<>("Property updated successfully", HttpStatus.OK);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProperty(@RequestParam Long ownerId, @RequestParam Long propertyId){
        propertyService.deleteOwnerProperty(ownerId, propertyId);
        return new ResponseEntity<>("Property deleted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/view") // It will return all properties of single specific owner
    public ResponseEntity<List<PropertyResponseDto>> viewPropertyOfSingleOwner(@RequestParam Long ownerId){
        return new ResponseEntity<>(propertyService.getPropertiesOfSingleOwner(ownerId), HttpStatus.OK);
    }
    
    @GetMapping("/view") // It will return all properties of entire owners 
    public ResponseEntity<List<PropertyResponseDto>> viewPropertyOfAllOwners(){
        return new ResponseEntity<>(propertyService.getPropertiesOfAllOwners(), HttpStatus.OK);
    }

}