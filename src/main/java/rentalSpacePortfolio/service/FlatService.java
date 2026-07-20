package rentalSpacePortfolio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.request.flat.FlatDataRequest;
import rentalSpacePortfolio.dto.request.flat.FlatImageDataRequest;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.FlatImage;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.enums.FlatStatus;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.FlatResponseMapper;
import rentalSpacePortfolio.repository.FlatImageRepository;
import rentalSpacePortfolio.repository.FlatRepository;
import rentalSpacePortfolio.repository.PropertyRepository;

@Slf4j
@Service
public class FlatService {
    
    private final PropertyRepository propertyRepo;
    private final FlatRepository flatRepo;
    private final FlatImageRepository flatImageRepo;
    private final ImageStorageService imageStorageService;
    
    @Autowired
    public FlatService(
            PropertyRepository propertyRepo,
            FlatRepository flatRepo,
            ImageStorageService imageStorageService,
            FlatImageRepository flatImageRepo){
        this.propertyRepo = propertyRepo;
        this.flatRepo = flatRepo;
        this.imageStorageService = imageStorageService;
        this.flatImageRepo = flatImageRepo;
    }
    
    // Service to get all flat of specific property by Id
    public List<FlatResponse> getPropertyAllFlat(UUID propertyId){
        propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                    log.info("Faile to fetch Flats! property not found with Id: {}", propertyId);
                    return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });
        
        List<Flat> flats = flatRepo.findAllPropertyFlat(propertyId);
        return flats.stream().map(flat -> FlatResponseMapper.mapToFlatDto(flat)).collect(Collectors.toList());
    }
    
    // Service to add flat in property
    @Transactional
    public void saveFlat(List<MultipartFile> images, 
            List<FlatImageDataRequest> imageDetails, 
            FlatDataRequest flatData,
            UUID propertyId) throws IOException, HttpMediaTypeNotSupportedException{
        
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow( () -> {
                        log.info("Failed to add Flat! property not found with Id: {}", propertyId);
                        return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                }
        );
        
        Flat flat = new Flat();
        mapToFlatDto(flat, flatData, property);
        Flat savedFlat = flatRepo.save(flat);
        log.info("Flat data added successfully, now adding images");
        
        savedFlat.setFlatImages(saveImageToStorage(images, imageDetails, flat));
        flatRepo.save(savedFlat);
        
        log.info("Property flat added successfully with Id: {}", savedFlat.getId());
        
    }
    
    // shared method to map flat dto to entity
    private Flat mapToFlatDto(Flat flat, FlatDataRequest flatData, Property property){
        flat.setBuildingName(flatData.getBuildingName());
        flat.setFlatNumber(flatData.getFlatNumber());
        flat.setFloorNumber(flatData.getFloorNumber());
        flat.setAreaSqFt(flatData.getAreaSqFt());
        flat.setType(flatData.getType());
        flat.setStatus(selectFlatStatus(flatData.getStatus()));
        flat.setRentAmount(flatData.getRentAmount());
        flat.setProperty(property);
        flat.setDeleted(false);
        
        return flat;
    }
    
    // method to set given flat status 
    private FlatStatus selectFlatStatus(String flatStatus){
     return switch (flatStatus.toUpperCase()){
            case "VACANT" -> FlatStatus.VACANT;
            case "OCCUPIED" -> FlatStatus.OCCUPIED;
            case "UNDER_MAINTENANCE" -> FlatStatus.UNDER_MAINTENANCE;
            default -> FlatStatus.VACANT;
        };
    }
    
    // shared method to save image to storage and propertyImage table
    private List<FlatImage> saveImageToStorage(
            List<MultipartFile> images,
            List<FlatImageDataRequest> FlatImageData,
            Flat flat) throws IOException, HttpMediaTypeNotSupportedException{
        
        List<FlatImage> flatImages = new ArrayList<>();
        
        for(int i = 0; i < images.size(); i++){
            MultipartFile file = images.get(i); // get actual image file
            FlatImageDataRequest req = FlatImageData.get(i); // get image details
            
            // save image to disk, get back URL
            String imageUrl = imageStorageService.saveImage(file, "flat");
            
            FlatImage image = new FlatImage();
            image.getImageDetails().setImageUrl(imageUrl);
            image.getImageDetails().setDisplayOrder(req.getDisplayOrder());
            image.getImageDetails().setIsCoverImage(req.getIsCoverImage());
            image.setFlat(flat);
            
            flatImages.add(image);
        }
        
        List<FlatImage> savedImages = flatImageRepo.saveAll(flatImages);
        
        return savedImages;
    }

}