package rentalSpacePortfolio.service.interfaces;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


public interface ImageStorageService {
    String upload(MultipartFile file, String imageFor) throws IOException;
    void delete(String folderPath) throws IOException;
}
