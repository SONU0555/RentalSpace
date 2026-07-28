package rentalSpacePortfolio.validation;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.exception.MaxUploadCountExceededException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class ImageValidator {
    
    // shared validation method for property, flat, and amenity
    public List<ImageRequest> validateAndParseRequest(List<MultipartFile> images, String imageDetails, int MAX_IMAGE_COUNT){

    if (images.size() > MAX_IMAGE_COUNT) {
        log.warn("Image upload failed: max limit is 5");
        throw new MaxUploadCountExceededException("Maximum file upload limit is 5");
    }

    ObjectMapper mapper = new ObjectMapper();
    List<ImageRequest> imageRequests = mapper.readValue(
            imageDetails, 
            mapper.getTypeFactory().constructCollectionType(List.class, ImageRequest.class)
    );

    if (imageRequests.size() > MAX_IMAGE_COUNT) {
        log.warn("Image validation failed: max JSON details limit is {}", MAX_IMAGE_COUNT);
        throw new MaxUploadCountExceededException("Maximum file upload limit is " + MAX_IMAGE_COUNT);
    }

    if (images.size() != imageRequests.size()) {
        throw new IllegalArgumentException("Images count and image details count must match");
    }

    return imageRequests;
    }

}