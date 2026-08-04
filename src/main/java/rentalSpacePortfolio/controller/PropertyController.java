package rentalSpacePortfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.impl.PropertyService;
import rentalSpacePortfolio.validation.ImageValidator;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE + "/owner/properties")
public class PropertyController {
        
    private final PropertyService propertyService;
    private final ImageValidator imageValidator;
    private static final int MAX_IMAGE_COUNT = 5;
    
    @Autowired
    public PropertyController(PropertyService propertyService, ImageValidator imageValidator){
        this.propertyService = propertyService;
        this.imageValidator = imageValidator;
    }
    
    // Endpoint to add new property
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProperty (
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("propertyData") PropertyRequest propertyData
            ) throws IOException{     
        
        log.info("Received request to add new property");
        
        String ownerId = SecurityUnits.getCurrentUserId();
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        propertyService.createProperty(images, imageRequests, propertyData, UUID.fromString(ownerId));
        return new ResponseEntity<>((new ApiResponse<>(true, "Property added successfully!", "Empty")), HttpStatus.CREATED); 
    }
    
    // Endpoint to update new property
    @PutMapping(value = "/{propertyId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProperty (
            @PathVariable("propertyId") UUID propertyId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("propertyData") PropertyRequest propertyData
            ) throws IOException{     
        
        log.info("Received request to update property with Id: {}", propertyId);
        
        String ownerId = SecurityUnits.getCurrentUserId();
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        propertyService.updateProperty(images, imageRequests, propertyData, propertyId, UUID.fromString(ownerId));
        return new ResponseEntity<>((new ApiResponse<>(true, "Property updated successfully!", "updated")), HttpStatus.CREATED); 
    }
    
    // Endpoint to soft delete property
    @PatchMapping("/{propertyId}")
    public ResponseEntity<Void> softDeleteProperty(
            @PathVariable("propertyId") UUID propertyId,
            @RequestParam("isActive") boolean isActive
    ) {
           propertyService.softDelete(propertyId, isActive);
        return ResponseEntity.noContent().build();
    }
        
    // Endpoint to assign admin to a specific property
    @PostMapping("/{propertyId}/assign")
    public ResponseEntity<ApiResponse<String>> assingAdminToProperty(@PathVariable("propertyId") UUID propertyId,
            @RequestParam("admin") UUID adminId){
        
        String ownerId = SecurityUnits.getCurrentUserId();
        log.info("Received request to assign adminId: {} to propertyId: {} by ownerId: {}", adminId, propertyId, ownerId);
        propertyService.assignPropertyToAdmin(propertyId, adminId, UUID.fromString(ownerId));
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin assigned successfully!", "Empty"));
    }
    
}