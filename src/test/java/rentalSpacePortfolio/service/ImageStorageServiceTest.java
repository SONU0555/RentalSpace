package rentalSpacePortfolio.service;

import rentalSpacePortfolio.service.interfaces.ImageStorageService;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageStorageServiceTest {
    
    private ImageStorageService imageStorageService;
    
    @Test
    void shouldThrowException_whenContentTypeNull(){
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(null);
        
        HttpMediaTypeNotSupportedException ex = Assertions.assertThrows(HttpMediaTypeNotSupportedException.class, () 
                -> imageStorageService.upload(file, "property"));
        
        assertEquals("Only JPG, WEBP, allowed", ex.getMessage());
    }
    
    @Test
    void shouldThrowException_whenContentTypeNotAllowed() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");

        HttpMediaTypeNotSupportedException ex = assertThrows(HttpMediaTypeNotSupportedException.class, () ->
            imageStorageService.upload(file, "property"));

        assertEquals("Only JPG, WEBP, allowed", ex.getMessage());
    }
    
    @Test
    void shouldThrowException_whenFileSizeExceedsMax() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            imageStorageService.upload(file, "property"));

        assertEquals("File size must not exceed 5MB", ex.getMessage());
    }

}