package rentalSpacePortfolio.commonService;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.common.service.ImageService;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.entity.BelongingImage;
import rentalSpacePortfolio.entity.ImageDetails;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    
    @Mock private ImageStorageService imageStorageService;

    @Mock private JpaRepository<TestImage, Long> mockRepository;

    @InjectMocks
    private ImageService imageService;
    
    static class TestParent{};
    
    static class TestImage implements BelongingImage<TestParent>{
        private ImageDetails details = new ImageDetails();
        private TestParent parent;

        @Override
        public ImageDetails getImageDetails() { return details;}

        @Override
        public void setParent(TestParent parentEntity) { this.parent = parentEntity;}
        public TestParent getParent(){ return parent;}
        
    }
    
    @Test
    void shouldMapAndSaveAllImagesSuccessfully() throws IOException{
        TestParent parent = new TestParent();
        String folderName = "property";
        
        List<MultipartFile> images = List.of(
            new MockMultipartFile("img1", "1.jpg", "image/jpeg", new byte[]{1}),
            new MockMultipartFile("img2", "2.jpg", "image/jpeg", new byte[]{2})
        );

        ImageRequest req1 = new ImageRequest(); req1.setDisplayOrder(1); req1.setIsCoverImage(true);
        ImageRequest req2 = new ImageRequest(); req2.setDisplayOrder(2); req2.setIsCoverImage(false);
        List<ImageRequest> imageRequests = List.of(req1, req2);
        
        Mockito.when(imageStorageService.upload(images.get(0), folderName)).thenReturn("http://s3/1.jpg");
        Mockito.when(imageStorageService.upload(images.get(1), folderName)).thenReturn("http://s3/2.jpg");
        Mockito.when(mockRepository.saveAll(Mockito.anyList())).thenAnswer(inv -> inv.getArgument(0));
                
        List<TestImage> result = imageService
                .mapAndSaveImage(images, imageRequests, parent, folderName, TestImage::new, mockRepository);
        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        
        // Verify First Image mapping
        TestImage firstResult = result.get(0);
        Assertions.assertEquals("http://s3/1.jpg", firstResult.getImageDetails().getImageUrl());
        Assertions.assertEquals(1, firstResult.getImageDetails().getDisplayOrder());
        Assertions.assertTrue(firstResult.getImageDetails().getIsCoverImage());
        Assertions.assertEquals(parent, firstResult.getParent());
        
        Mockito.verify(mockRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }
    
    @Test
    void shouldThrowException_WhenUploadImageFails() throws IOException{
        TestParent parent = new TestParent();
        List<MultipartFile> images = List.of(new MockMultipartFile("image", new byte[]{1}));
        List<ImageRequest> details = List.of(new ImageRequest());
        
        Mockito.when(imageStorageService.upload(Mockito.any(), Mockito.anyString()))
                .thenThrow(new IOException("S3 connection timeout"));
        
        Assertions.assertThrows(IOException.class, () -> {
            imageService.mapAndSaveImage(images, details, parent, "flat", TestImage::new, mockRepository);
        });
        
        Mockito.verify(mockRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

}