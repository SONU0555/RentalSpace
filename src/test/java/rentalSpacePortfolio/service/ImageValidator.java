package rentalSpacePortfolio.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.exception.MaxUploadCountExceededException;
import rentalSpacePortfolio.validation.ImageValidatorService;


@ExtendWith(MockitoExtension.class)
public class ImageValidator {
    
    private ImageValidatorService ImageValidator = new ImageValidatorService();
    private final int MAX_LIMIT = 5;
    
    @Test
    void validateAndReturnImages_WhenInputAreValid(){
        
        List<MultipartFile> images = List.of(
                new MockMultipartFile("image1", new byte[1]),
                new MockMultipartFile("image2", new byte[2])
                );
        
        List<ImageRequest> imageDetails = List.of(new ImageRequest(), new ImageRequest());
        List<ImageRequest> result = ImageValidator.validateImageAndDetails(images, imageDetails, MAX_LIMIT);
        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
    
    @Test
    void shouldThrowException_WhenImagesExceedLimit(){
        List<MultipartFile> images = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            images.add(new MockMultipartFile("image", new byte[i]));
        }
        
        List<ImageRequest> details = List.of(new ImageRequest());
        
        MaxUploadCountExceededException exception = Assertions
                .assertThrows(MaxUploadCountExceededException.class, () -> {
                    ImageValidator.validateImageAndDetails(images, details, MAX_LIMIT);
                });
        
        Assertions.assertEquals("Maximum file and metadata upload limit is 5", exception.getMessage());
    }
    
    @Test
    void shouldThrowException_WhenImageDetailsExceedLimit(){
        List<ImageRequest> imageDetails = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            imageDetails.add(new ImageRequest());
        }
        
        List<MultipartFile> images = List.of(new MockMultipartFile("image", new byte[1]));
        
        MaxUploadCountExceededException exception = Assertions
                .assertThrows(MaxUploadCountExceededException.class, () -> {
                    ImageValidator.validateImageAndDetails(images, imageDetails, MAX_LIMIT);
                });
        
        Assertions.assertEquals("Maximum file and metadata upload limit is 5", exception.getMessage());
    }
    
    @Test
    void shouldThrowException_WhenImagesAndDetailsSizeNotSame(){
        List<MultipartFile> images = List.of(new MockMultipartFile("image", new byte[1]));
        List<ImageRequest> imageDetails = List.of(new ImageRequest(), new ImageRequest());
        
        IllegalArgumentException exception = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    ImageValidator.validateImageAndDetails(images, imageDetails, MAX_LIMIT);
                });
        
        Assertions.assertEquals("Images count and image details count must match", exception.getMessage());
    }

}