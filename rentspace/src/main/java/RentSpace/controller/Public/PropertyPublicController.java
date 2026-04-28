package RentSpace.controller.Public;

import RentSpace.responseDto.PropertyResponseDto;
import RentSpace.service.PropertyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public/property")
public class PropertyPublicController {
    
    private final PropertyService propertyService;
    
    @Autowired
    public PropertyPublicController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    @GetMapping("/owner/{ownerId}/all") // It will return all properties of single specific owner
    public ResponseEntity<List<PropertyResponseDto>> viewPropertyOfSingleOwner(@PathVariable Long ownerId){
        return new ResponseEntity<>(propertyService.getPropertiesOfSingleOwner(ownerId), HttpStatus.OK);
    }
    
    @GetMapping("/all") // It will return all properties of entire owners 
    public ResponseEntity<List<PropertyResponseDto>> viewPropertyOfAllOwners(){
        return new ResponseEntity<>(propertyService.getPropertiesOfAllOwners(), HttpStatus.OK);
    }

}