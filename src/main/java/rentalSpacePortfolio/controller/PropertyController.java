package rentalSpacePortfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import rentalSpacePortfolio.dto.request.property.PropertyImageRequest;
import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.exception.MaxUploadCountExceededException;
import rentalSpacePortfolio.security.SecurityUnits;
import rentalSpacePortfolio.service.PropertyService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE + "/owner/properties")
public class PropertyController {
        
    private final PropertyService propertyService;
    
    @Autowired
    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    
    // shared validation method for add and update property
    private List<PropertyImageRequest> validateAndParseRequest(
            int step, String tab, List<MultipartFile> images, String imageDetails){
        
    if (step != 0 || !tab.equals("property")) {
        log.warn("Validation failed: requested path step or tab data is wrong");
        throw new IllegalArgumentException("Incorrect Path");
    }

    if (images.size() > 5) {
        log.warn("Image upload failed: max limit is 5");
        throw new MaxUploadCountExceededException("Maximum file upload limit is 5");
    }

    ObjectMapper mapper = new ObjectMapper();
    List<PropertyImageRequest> imageRequests = mapper.readValue(
            imageDetails, 
            mapper.getTypeFactory().constructCollectionType(List.class, PropertyImageRequest.class)
    );

    if (imageRequests.size() > 5) {
        log.warn("Image validation failed: max JSON details limit is 5");
        throw new MaxUploadCountExceededException("Maximum file upload limit is 5");
    }

    if (images.size() != imageRequests.size()) {
        throw new IllegalArgumentException("Images count and image details count must match");
    }

    return imageRequests;
    }
    
    // Endpoint to add new property
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProperty (
            @RequestParam("step") int step,
            @RequestParam("tab") String tab,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("propertyData") PropertyRequest propertyData
            ) throws IOException, HttpMediaTypeNotSupportedException{     
        
        log.info("Received request to add new property");
        
        String ownerId = SecurityUnits.getCurrentUserId();
        List<PropertyImageRequest> imageRequests = validateAndParseRequest(step, tab, images, imageDetails);
        
        propertyService.saveProperty(images, imageRequests, propertyData, UUID.fromString(ownerId));
        return new ResponseEntity<>((new ApiResponse<>(true, "Property added successfully!", "Empty")), HttpStatus.CREATED); 
    }
    
    // Endpoint to update new property
    @PutMapping(value = "/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProperty (
            @RequestParam("step") int step,
            @RequestParam("tab") String tab,
            @PathVariable("propertyId") UUID propertyId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("propertyData") PropertyRequest propertyData
            ) throws IOException, HttpMediaTypeNotSupportedException{     
        
        log.info("Received request to update property with Id: {}", propertyId);
        
        String ownerId = SecurityUnits.getCurrentUserId();
        List<PropertyImageRequest> imageRequests = validateAndParseRequest(step, tab, images, imageDetails);
        
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