package rentalSpacePortfolio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.request.property.PropertyImageRequest;
import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.PropertyImage;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.PropertyStatus;
import rentalSpacePortfolio.exception.DuplicatePropertyException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.repository.PropertyImageRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
    
    @Mock private UserRepository userRepo;
    @Mock private PropertyRepository propertyRepo;
    @Mock private PropertyImageRepository propertyImgRepo;
    @Mock private ImageStorageService imageStorageService;
    
    @InjectMocks
    private PropertyService propertyService;
    
    private UUID ownerId;
    private User owner;
    private PropertyRequest propertyData;
    
    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        owner = new User();
        owner.setId(ownerId);
        
        propertyData = new PropertyRequest();
        propertyData.setName("Green Villa");
        propertyData.setDescription("A nice place");
        propertyData.setAddress("123 Main St");
        propertyData.setCity("Pune");
        propertyData.setState("MH");
        propertyData.setPinCode("411001");
        propertyData.setStatus("AVAILABLE");
        propertyData.setMinimumRent(20000.0);
        propertyData.setMaximumRent(50000.0);
    }
    
    @Test
    void shouldThrowException_whenOwnerNotFound() throws IOException{
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                propertyService.addProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verifyNoInteractions(propertyRepo);
    }
    
    @Test
    void shouldThrowException_whenPropertyAlreadyExist() throws IOException{
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(true);
        
        Assertions.assertThrows(DuplicatePropertyException.class, () -> 
                propertyService.addProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verify(propertyRepo, Mockito.never()).save(Mockito.any());
    }
    
    @Test
    void shouldSaveProperty_whenCorrectFieldMapping() throws IOException{
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        
        propertyService.addProperty(List.of(), List.of(), propertyData, ownerId);
        
        ArgumentCaptor<Property> captor = ArgumentCaptor.forClass(Property.class);
        Mockito.verify(propertyRepo, Mockito.atLeastOnce()).save(captor.capture());
        
        Property saved = captor.getAllValues().get(0);
        
        Assertions.assertAll("property field mapping",
            () -> Assertions.assertEquals("Green Villa", saved.getName()),
            () -> Assertions.assertEquals("A nice place", saved.getDescription()),
            () -> Assertions.assertEquals("123 Main St", saved.getAddress()),
            () -> Assertions.assertEquals("Pune", saved.getCity()),
            () -> Assertions.assertEquals("MH", saved.getState()),
            () -> Assertions.assertEquals("411001", saved.getPinCode()),
            () -> Assertions.assertEquals(20000.0, saved.getMiniumRent()),
            () -> Assertions.assertEquals(50000.0, saved.getMaximumRent()),
            () -> Assertions.assertEquals(owner, saved.getOwner())
        );
    }
    
    @Test
    void shouldSaveProperty_whenCorrectImageDetails() throws IOException{
        MultipartFile file1 = Mockito.mock(MultipartFile.class);
        MultipartFile file2 = Mockito.mock(MultipartFile.class);
        
        PropertyImageRequest req1 = new PropertyImageRequest(true, 1);
        PropertyImageRequest req2 = new PropertyImageRequest(false, 2);
        
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(imageStorageService.saveImage(Mockito.eq(file1), Mockito.anyString())).thenReturn("url1");
        Mockito.when(imageStorageService.saveImage(Mockito.eq(file2), Mockito.anyString())).thenReturn("url2");
        
        propertyService.addProperty(
            List.of(file1, file2), List.of(req1, req2), propertyData, ownerId);
        
        ArgumentCaptor<List<PropertyImage>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(propertyImgRepo).saveAll(captor.capture());
        
        List<PropertyImage> savedImages = captor.getValue();
        Assertions.assertEquals("url1", savedImages.get(0).getImageUrl());
        Assertions.assertEquals(1, savedImages.get(0).getDisplayOrder());
        Assertions.assertTrue(savedImages.get(0).getIsCoverImage());
        
    }
    
    @Test
    void shouldSaveImage_whenNoImagesProvided() throws IOException{
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        
        propertyService.addProperty(List.of(), List.of(), propertyData, ownerId);

       Mockito.verify(propertyImgRepo).saveAll(Collections.emptyList());
       Mockito.verifyNoInteractions(imageStorageService);
    }

}