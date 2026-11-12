package rentalSpacePortfolio.service;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.exception.InvalidFileTypeException;
import rentalSpacePortfolio.service.impl.LocalDiskImageUploadService;

@ExtendWith(MockitoExtension.class)
public class ImageStorageServiceTest {
    
    private LocalDiskImageUploadService imageStorageService = new LocalDiskImageUploadService();
    
    @Test
    void shouldSaveToDiskAndReturnPath_WhenFileIsValid() throws IOException{
        byte[] content = "fake-image-content".getBytes();
        MockMultipartFile validFile = new MockMultipartFile("file", "property.jpg", "image/jpeg", content);
        
        String savedPathString = imageStorageService.upload(validFile, "property");
        
        Assertions.assertNotNull(savedPathString);
        Assertions.assertTrue(savedPathString.endsWith(".jpg"));
        Assertions.assertTrue(savedPathString.contains("property/"));
        
        Path savedFilePath = Path.of(savedPathString);
        Assertions.assertTrue(Files.exists(savedFilePath));
        Assertions.assertArrayEquals(content, Files.readAllBytes(savedFilePath));
        
    }
    
    @Test
    void shouldThrowException_whenContentTypeIsNull(){
        byte[] content = "fake-image-content".getBytes();
        MockMultipartFile fileWithNull = new MockMultipartFile("file", "flat.jpg", null, content);
        
        InvalidFileTypeException ex = Assertions.assertThrows(InvalidFileTypeException.class, () 
                -> imageStorageService.upload(fileWithNull, "property"));
        
        assertEquals("Only JPG, WEBP, allowed", ex.getMessage());
    }
    
    @Test
    void shouldThrowException_whenContentTypeNotAllowed() {
        MockMultipartFile invalidFileType = new MockMultipartFile("file", "document.pdg", "application/pdf", new byte[]{1,2});

        InvalidFileTypeException ex = assertThrows(InvalidFileTypeException.class, () ->
            imageStorageService.upload(invalidFileType, "property"));

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
        
        InvalidFileTypeException ex = assertThrows(InvalidFileTypeException.class, () -> {
            imageStorageService.upload(file, "Flat");
        });
        
        assertEquals("File must have a valid name and extension", ex.getMessage());
    }

}