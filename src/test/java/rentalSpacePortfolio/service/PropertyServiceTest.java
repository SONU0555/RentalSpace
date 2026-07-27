package rentalSpacePortfolio.service;

import rentalSpacePortfolio.service.impl.PropertyService;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;
import java.io.IOException;
import java.util.Collections;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.request.image.ImageRequest;
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
        propertyData.setStatus(PropertyStatus.AVAILABLE);
        propertyData.setMinimumRent(20000.0);
        propertyData.setMaximumRent(50000.0);
    }
    
    @Test
    void shouldThrowException_whenOwnerNotFound() throws IOException{
        when(userRepo.findById(ownerId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                propertyService.createProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verifyNoInteractions(propertyRepo);
    }
    
    @Test
    void shouldThrowException_whenPropertyAlreadyExist() throws IOException{
        when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(true);
        
        Assertions.assertThrows(DuplicatePropertyException.class, () -> 
                propertyService.createProperty(List.of(), List.of(), propertyData, ownerId));
        
        Mockito.verify(propertyRepo, Mockito.never()).save(Mockito.any());
    }
    
    @Test
    void shouldSaveProperty_whenCorrectFieldMapping() throws IOException, HttpMediaTypeNotSupportedException{
        Mockito.when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        Mockito.when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        Mockito.when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        
        propertyService.createProperty(List.of(), List.of(), propertyData, ownerId);
        
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
            () -> Assertions.assertEquals(20000.0, saved.getMinimumRent()),
            () -> Assertions.assertEquals(50000.0, saved.getMaximumRent()),
            () -> Assertions.assertEquals(owner, saved.getOwner())
        );
    }
    
    @Test
    void shouldSaveProperty_whenCorrectImageDetails() throws IOException, HttpMediaTypeNotSupportedException{
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        
        ImageRequest req1 = new ImageRequest(null, true, 1);
        ImageRequest req2 = new ImageRequest(null, false, 2);
        
        when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        when(imageStorageService.upload(Mockito.eq(file1), Mockito.anyString())).thenReturn("url1");
        when(imageStorageService.upload(Mockito.eq(file2), Mockito.anyString())).thenReturn("url2");
        
        
        propertyService.createProperty(List.of(file1, file2), List.of(req1, req2), propertyData, ownerId);
        
        ArgumentCaptor<List<PropertyImage>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(propertyImgRepo).saveAll(captor.capture());
        
        List<PropertyImage> savedImages = captor.getValue();
        Assertions.assertEquals("url1", savedImages.get(0).getImageDetails().getImageUrl());
        Assertions.assertEquals(1, savedImages.get(0).getImageDetails().getDisplayOrder());
        Assertions.assertTrue(savedImages.get(0).getImageDetails().getIsCoverImage());
        
    }
    
    @Test
    void shouldSaveImage_whenNoImagesProvided() throws IOException, HttpMediaTypeNotSupportedException{
        when(userRepo.findById(ownerId)).thenReturn(Optional.of(owner));
        when(propertyRepo.existsByAddress(propertyData.getAddress())).thenReturn(false);
        when(propertyRepo.save(Mockito.any(Property.class))).thenAnswer(inv -> inv.getArgument(0));
        
        propertyService.createProperty(List.of(), List.of(), propertyData, ownerId);

       Mockito.verify(propertyImgRepo).saveAll(Collections.emptyList());
       Mockito.verifyNoInteractions(imageStorageService);
    }

}