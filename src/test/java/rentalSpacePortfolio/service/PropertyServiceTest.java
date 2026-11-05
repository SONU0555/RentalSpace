package rentalSpacePortfolio.service;

import rentalSpacePortfolio.service.impl.PropertyService;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.PropertyImage;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.exception.DuplicatePropertyException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.repository.PropertyImageRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.service.impl.CommonService;


@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
    
    @Mock private UserRepository userRepo;
    @Mock private PropertyRepository propertyRepo;
    @Mock private PropertyImageRepository propertyImgRepo;
    @Mock private ImageStorageService imageStorageService;
    @Mock private CommonService commonService;
    
    @InjectMocks
    private PropertyService propertyService;
    
    private UUID ownerId;
    private User owner;
    private PropertyRequest propertyData;
    private List<MultipartFile> images;
    private List<ImageRequest> imageDetails;
    
    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        owner = new User();
        owner.setId(ownerId);
        
        propertyData = new PropertyRequest();
        propertyData.setName("Green Villa");
        propertyData.setAddress("123 Main St");
    }
    
    @Test
    void shouldCreatePropertySuccessfully() throws IOException{
        Property savedProperty = new Property();
        savedProperty.setId(UUID.randomUUID());
        
        List<PropertyImage> savedImages = List.of(new PropertyImage());
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenReturn(savedProperty);
        Mockito.when(commonService.mapAndSaveImage(
                Mockito.eq(images), Mockito.eq(imageDetails), Mockito.same(savedProperty),
                Mockito.eq("property"), Mockito.any(), Mockito.same(propertyImgRepo)
        )).thenReturn(savedImages);
        
        Assertions.assertDoesNotThrow(() -> {
            propertyService.createProperty(images, imageDetails, propertyData, ownerId);
        });
        
        Mockito.verify(userRepo, Mockito.times(1)).findById(ownerId);
        
        Mockito.verify(propertyRepo, Mockito.times(1)).existsByAddress(propertyData.getAddress());
        
        ArgumentCaptor<Property> captor = ArgumentCaptor.forClass(Property.class);
        Mockito.verify(propertyRepo, Mockito.times(2)).save(captor.capture());
        List<Property> capturedProperties = captor.getAllValues();
        
//        Assertions.assertSame(capturedProperties.get(0), capturedProperties.get(1));
        
        Assertions.assertEquals(propertyData.getAddress(), capturedProperties.get(0).getAddress());
        
        Mockito.verify(commonService, Mockito.times(1)).mapAndSaveImage(
                Mockito.eq(images), Mockito.eq(imageDetails), Mockito.same(savedProperty),
                Mockito.eq("property"), Mockito.any(), Mockito.same(propertyImgRepo)
        );
        
        Assertions.assertEquals(savedImages, capturedProperties.get(1).getPropertyImages());
    }
    
    @Test
    void shouldThrowException_whenOwnerNotFound() throws IOException{
        when(userRepo.findById(ownerId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                propertyService.createProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verifyNoInteractions(commonService, propertyRepo, propertyImgRepo);
    }
    
    @Test
    void shouldThrowException_whenPropertyAlreadyExist() throws IOException{
        when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(true);
        
        Assertions.assertThrows(DuplicatePropertyException.class, () -> 
                propertyService.createProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verify(userRepo, Mockito.times(1)).findById(ownerId);
        
        Mockito.verify(propertyRepo, Mockito.never()).save(Mockito.any(Property.class));
        Mockito.verifyNoInteractions(commonService, propertyImgRepo);
    }
    
    @Test
    void shouldThrowException_WhenImageSavingFails() throws IOException{
        Property savedProperty = new Property();
        savedProperty.setId(UUID.randomUUID());
        
        IOException ioException = new IOException("Image storage failed");
       
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenReturn(savedProperty);
        Mockito.when(commonService.mapAndSaveImage(
                Mockito.eq(images), Mockito.eq(imageDetails), Mockito.same(savedProperty),
                Mockito.eq("property"), Mockito.any(), Mockito.same(propertyImgRepo)
        )).thenThrow(ioException);
        
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            propertyService.createProperty(images, imageDetails, propertyData, ownerId);
        });
        
        Assertions.assertEquals("Failed to save property images", exception.getMessage());
        Assertions.assertSame(ioException, exception.getCause());
        
        Mockito.verify(propertyRepo, Mockito.times(1)).save(Mockito.any(Property.class));
        Mockito.verify(commonService).mapAndSaveImage(
                Mockito.eq(images), Mockito.eq(imageDetails), Mockito.same(savedProperty),
                Mockito.eq("property"), Mockito.any(), Mockito.same(propertyImgRepo)
        );       
    }
    
    @Test
    void shouldCreateProperty_whenNoImagesProvided() throws IOException{
        Property savedProperty = new Property();
        savedProperty.setId(UUID.randomUUID());
        
        when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        when(propertyRepo.save(Mockito.any(Property.class))).thenReturn(savedProperty);
        Mockito.when(commonService.mapAndSaveImage(
                Mockito.eq(List.of()), Mockito.eq(List.of()), Mockito.same(savedProperty),
                Mockito.eq("property"), Mockito.any(), Mockito.same(propertyImgRepo)
        )).thenReturn(List.of());
        
        Assertions.assertDoesNotThrow(() -> {
            propertyService.createProperty(List.of(), List.of(), propertyData, ownerId);
        });
        
        Mockito.verify(propertyRepo, Mockito.times(2)).save(Mockito.any(Property.class));
        Assertions.assertTrue(savedProperty.getPropertyImages().isEmpty());
    }
}