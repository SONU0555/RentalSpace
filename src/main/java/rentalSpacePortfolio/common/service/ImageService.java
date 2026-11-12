package rentalSpacePortfolio.common.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.entity.BelongingImage;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;

@Slf4j
@Service
public class ImageService {
        
    private final ImageStorageService imageStorageService;
    
    @Autowired
    public ImageService(
            ImageStorageService imageStorageService){
        this.imageStorageService = imageStorageService;
    }
    
    // Service to store image to local disk and map to entity
    public <T, I extends BelongingImage<T>> List<I> mapAndSaveImage(
                List<MultipartFile> images,
                List<ImageRequest> imageRequests,
                T parentEntity,
                String folderName,
                Supplier<I> imageSupplier,
                JpaRepository<I, ?> repository) throws IOException{
    
        List<I> processedImageList = new ArrayList<>();
        
        for(int i = 0; i < images.size(); i++){
            MultipartFile file = images.get(i); // get actual image file
            ImageRequest req = imageRequests.get(i); // get image details
            
            // save image to disk, get back URL
            String imageUrl = imageStorageService.upload(file, folderName);
            
            I image = imageSupplier.get();
            image.getImageDetails().setImageUrl(imageUrl);
            image.getImageDetails().setDisplayOrder(req.getDisplayOrder());
            image.getImageDetails().setIsCoverImage(req.getIsCoverImage());
            image.setParent(parentEntity);
 
            processedImageList.add(image);
        }
                
        return repository.saveAll(processedImageList);    
    }
    
}