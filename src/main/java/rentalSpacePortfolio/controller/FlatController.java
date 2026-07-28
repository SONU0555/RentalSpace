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
import rentalSpacePortfolio.dto.request.flat.FlatDataRequest;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.service.impl.FlatService;
import rentalSpacePortfolio.validation.ImageValidator;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE)
public class FlatController {
    
    private final FlatService flatService;
    private final ImageValidator imageValidator;
    private static final int MAX_IMAGE_COUNT = 5;
    
    @Autowired
    public FlatController(FlatService flatService, ImageValidator imageValidator){
        this.flatService = flatService;
        this.imageValidator = imageValidator;
    }
    
    // Endpoint to add property flats
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/owner/properties/{propertyId}/flats/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addFlat(
            @PathVariable UUID propertyId,
//            @RequestParam("step") int step,
//            @RequestParam("tab") String tab,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("flatData") FlatDataRequest flatData
            ) throws IOException{
        
        log.info("Received request to add new property flat");
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        flatService.saveFlat(images, imageRequests, flatData, propertyId);
        return new ResponseEntity<>((new ApiResponse<>(true, "Flat added successfully!", "Empty")), HttpStatus.CREATED);
    }
    
    // Endpoint to update property flat
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping(value = "/owner/flats/{flatId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFlat (
//            @RequestParam("step") int step,
//            @RequestParam("tab") String tab,
            @PathVariable("flatId") UUID flatId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("flatData") FlatDataRequest flatData
            ) throws IOException{
        
        log.info("Received request to update flat with Id: {}", flatId);
        
        List<ImageRequest> imageRequests = imageValidator.validateAndParseRequest(images, imageDetails, MAX_IMAGE_COUNT);
        
        flatService.updateFlat(images, imageRequests, flatData, flatId);
        return new ResponseEntity<>((new ApiResponse<>(true, "flat updated successfully!", "updated")), HttpStatus.OK); 
    }
    
    // Endpoint to soft delete property
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/owner/flats/{flatId}")
    public ResponseEntity<Void> softDeleteFlat(
            @PathVariable("flatId") UUID flatId,
            @RequestParam("isActive") boolean isActive
    ) {
           flatService.softDelete(flatId, isActive);
        return ResponseEntity.noContent().build();
    }
    
    // Endpoint to get all flats
    @PreAuthorize("hasRole('TENANT')")
    @GetMapping("/properties/{propertyId}/flats")
    public ResponseEntity<ApiResponse<List<FlatResponse>>> getPropertyAllFlat(
            @PathVariable("propertyId") UUID propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size){
        log.info("Received request to fetch all flat of property: {}", propertyId);
        List<FlatResponse> flats = flatService.getPropertyAllFlat(propertyId, page, size);
        return ResponseEntity.ok(ApiResponse.success("All properties fetched succesfully", flats));
    }

}