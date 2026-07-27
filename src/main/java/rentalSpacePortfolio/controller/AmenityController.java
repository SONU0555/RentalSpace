package rentalSpacePortfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.constants.ApiPaths;
import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.dto.request.image.ImageRequest;
import rentalSpacePortfolio.dto.response.ApiResponse;
import rentalSpacePortfolio.exception.MaxUploadCountExceededException;
import rentalSpacePortfolio.service.impl.AmenityService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@RequestMapping(ApiPaths.BASE)
public class AmenityController {
    
    public final AmenityService amenityService;
    
    public AmenityController(AmenityService amenityService){
        this.amenityService = amenityService;
    }
    
    //shared validation method for create and update flat
    private List<ImageRequest> validateAndParseRequest(
             List<MultipartFile> images,
             String imageDetails){
        
//    if (step != 2 || !tab.equals("amenity")) {
//        log.warn("Validation failed: requested path step or tab data is wrong");
//        throw new IllegalArgumentException("Incorrect Path");
//    }

    if (images.size() > 5) {
        log.warn("Image upload failed: max limit is 5");
        throw new MaxUploadCountExceededException("Maximum file upload limit is 5");
    }

    ObjectMapper mapper = new ObjectMapper();
    List<ImageRequest> imageRequests = mapper.readValue(
            imageDetails, 
            mapper.getTypeFactory().constructCollectionType(List.class, ImageRequest.class)
    );

    if (imageRequests.size() > 5) {
        log.warn("Image validation failed: max JSON details size limit is 5");
        throw new MaxUploadCountExceededException("Maximum file upload limit is 5");
    }

    if (images.size() != imageRequests.size()) {
        throw new IllegalArgumentException("Images count and image details count must match");
    }

    return imageRequests;
    }
    
    // Endpoint to create new property amenity
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/owner/properties/{propertyId}/amenity/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> createAmenity(
            @PathVariable UUID propertyId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("imageDetails") String imageDetails,
            @RequestPart("amenityData") AmenityRequest amenityData
            ) throws IOException{
        
        log.info("Received request to create new property amenity");
        List<ImageRequest> imageRequests = validateAndParseRequest(images, imageDetails);
        
        amenityService.createAmenity(images, imageRequests, amenityData, propertyId);
        return new ResponseEntity<>((new ApiResponse<>(true, "Amenity created successfully!", "created")), HttpStatus.CREATED);
    }

}