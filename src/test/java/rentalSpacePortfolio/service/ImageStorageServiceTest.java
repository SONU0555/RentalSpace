package rentalSpacePortfolio.service;

import java.io.IOException;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.exception.InvalidFileTypeException;
import rentalSpacePortfolio.service.impl.LocalDiskImageUploadService;

@ExtendWith(MockitoExtension.class)
public class ImageStorageServiceTest {
    
    private LocalDiskImageUploadService imageStorageService;
    
    @BeforeEach
    void setUp(){
        imageStorageService = new LocalDiskImageUploadService();
        
        
    }
    
    @Test
    void shouldThrowException_whenContentTypeNull(){
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(null);
        
        InvalidFileTypeException ex = Assertions.assertThrows(InvalidFileTypeException.class, () 
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
    
    @Test
    void shouldThrowException_whenFileExtensionNotPresent(){
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(5 * 1024 * 1024L); // 5MB
        
        IOException ex = assertThrows(IOException.class, () -> {
            imageStorageService.upload(file, "Flat");
        });
        
        assertEquals("File must have a valid name and extension", ex.getMessage());
    }

}