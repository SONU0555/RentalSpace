package rentalSpacePortfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.response.Amenity.AmenityResponse;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.service.impl.AmenityService;
import rentalSpacePortfolio.validation.ImageValidator;


@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE)
public class AmenityController {
    
    public final AmenityService amenityService;
    private final ImageValidator imageValidator;
    private static final int MAX_IMAGE_COUNT = 5;
    
    public AmenityController(AmenityService amenityService, ImageValidator imageValidator){
        this.amenityService = amenityService;
        this.imageValidator = imageValidator;
    }
    
    // Endpoint to get all amenities
    @PreAuthorize("hasRole('TENANT')")
    @GetMapping("/properties/{propertyId}/amenities")
    public ResponseEntity<ApiResponse<List<AmenityResponse>>> getPropertyAllAmenity(
            @PathVariable("propertyId") UUID propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size){
        log.info("Received request to fetch all flat of property: {}", propertyId);
        List<AmenityResponse> amenities = amenityService.getPropertyAllAmenity(propertyId, page, size);
        return ResponseEntity.ok(ApiResponse.success("All properties fetched succesfully", amenities));
    }
    
    // Endpoint to create new amenity for property
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/owner/properties/{propertyId}/amenity/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createAmenity(
            @PathVariable UUID propertyId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("amenityData") AmenityRequest amenityData
            ) throws IOException{
        
        log.info("Received request to create new property amenity");
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        amenityService.createAmenity(images, imageRequests, amenityData, propertyId);
        return new ResponseEntity<>((new ApiResponse<>(true, "Amenity created successfully!", "created")), HttpStatus.CREATED);
    }
    
    // Endpoint to update amenity
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping(value = "/owner/amenities/{amenityId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateFlat (
            @PathVariable("amenityId") UUID amenityId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("amenityData") AmenityRequest amenityData
            ) throws IOException{
        
        log.info("Received request to update amenity with Id: {}", amenityId);
        
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        amenityService.updateAmenity(images, imageRequests, amenityData, amenityId);
        return new ResponseEntity<>((new ApiResponse<>(true, "amenity updated successfully!", "updated")), HttpStatus.OK); 
    }
    
    // Endpoint to soft delete property
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/owner/amenities/{amenityId}")
    public ResponseEntity<Void> softDeleteAmenity(
            @PathVariable("amenityId") UUID amenityId,
            @RequestParam("isActive") boolean isActive
    ) {
        amenityService.softDelete(amenityId, isActive);
        return ResponseEntity.noContent().build();
    }

}