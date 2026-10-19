package rentalSpacePortfolio.validation;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.exception.MaxUploadCountExceededException;

@Slf4j
@Component
public class ImageValidatorService {
    
    // shared validation method for property, flat, and amenity
    public List<ImageRequest> validateImageAndDetails(List<MultipartFile> images, List<ImageRequest> imageDetails, int MAX_IMAGE_COUNT){

    if (images.size() > MAX_IMAGE_COUNT || imageDetails.size() > MAX_IMAGE_COUNT) {
        log.warn("Image upload failed: max limit is {}", MAX_IMAGE_COUNT);
        throw new MaxUploadCountExceededException("Maximum file and metadata upload limit is 5");
    }

    if (images.size() != imageDetails.size()) {
        throw new IllegalArgumentException("Images count and image details count must match");
    }

    return imageDetails;
    }

}