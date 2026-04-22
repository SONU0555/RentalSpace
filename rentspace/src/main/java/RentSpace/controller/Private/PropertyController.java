package RentSpace.controller.Private;

import RentSpace.requestDtos.Property.AddPropertyDto;
import RentSpace.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class PropertyController {
    
    private final PropertyService propertyService;
    
    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addProperty(@Valid @RequestBody AddPropertyDto request, @RequestParam Long id){
        try{
            propertyService.addNewProperty(request, id);
            return new ResponseEntity<>("Property added successfully", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}