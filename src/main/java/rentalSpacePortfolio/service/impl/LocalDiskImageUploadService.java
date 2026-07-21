package rentalSpacePortfolio.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.exception.InvalidFileTypeException;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;

@Slf4j
@Component
public class LocalDiskImageUploadService implements ImageStorageService{
    
    @Value("${app.upload-dir}")
    private String UPLOAD_DIR;
    
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/webp");
    
    private static final long MAX_SIZE = 5 * 1024 * 1024;
    
    private void validateFile(MultipartFile file) throws IOException{
        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            log.warn("Upload image failed! file type not allowed: {}", contentType);
            throw new InvalidFileTypeException("Only JPG, WEBP, allowed");
        }
        
        // Validate file size
        if (file.getSize() > MAX_SIZE) {
            log.warn("Upload image failed! file must not exceed 5MB");
            throw new RuntimeException("File size must not exceed 5MB");
        }
    }

    @Override
    public String upload(MultipartFile file, String imageFor) throws IOException {
        validateFile(file);

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new InvalidFileTypeException("File must have a valid name and extension");
        }
        String extension = originalName.substring(originalName.lastIndexOf("."));
        
        String uniqueName = UUID.randomUUID().toString() + extension;
        
        Path folderPath = Path.of(UPLOAD_DIR + imageFor + "/");
        Files.createDirectories(folderPath);
        
        Path filePath = folderPath.resolve(uniqueName);
        Files.write(filePath, file.getBytes());
        
        return UPLOAD_DIR + imageFor + "/" + uniqueName;
    }

    @Override
    public void delete(String imageFilePath) throws IOException{
        log.info("Method called to delete image file path: {} form disk", imageFilePath);
        Path path = Path.of(imageFilePath);
        Files.delete(path);
        
        log.info("Image file path deleted successfully");
    }

}