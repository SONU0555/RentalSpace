package rentalSpacePortfolio.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.dto.request.image.ImageRequest;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.AmenityMapper;
import rentalSpacePortfolio.repository.AmenityImageRepository;
import rentalSpacePortfolio.repository.AmenityRepository;
import rentalSpacePortfolio.repository.PropertyRepository;

@Slf4j
@Service
public class AmenityService {
    
    private final AmenityRepository amenityRepo;
    private final PropertyRepository propertyRepo;
    private final AmenityImageRepository amenityImageRepo;
    private final CommonService commonService;
    
    @Autowired
    public AmenityService(
            AmenityRepository amenityRepo,
            PropertyRepository propertyRepo,
            AmenityImageRepository amenityImageRepo,
            CommonService commonService){
        this.amenityRepo = amenityRepo;
        this.propertyRepo = propertyRepo;
        this.amenityImageRepo = amenityImageRepo;
        this.commonService = commonService;
    }
    
    // Service to add amenity
    @Transactional
    public void createAmenity(List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            AmenityRequest amenityData,
            UUID propertyId) throws IOException{
        
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow( () -> {
                        log.info("Failed to add amenity! property not found with Id: {}", propertyId);
                        return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                }
        );
        
        Amenity amenity = new Amenity();
        AmenityMapper.mapToAmenityDto(amenity, amenityData, property);
        amenityRepo.save(amenity);
        log.info("Amenity data added successfully, now adding images");
        
        amenity.setImages(commonService.mapAndSaveImage(
                images, imageDetails, amenity, "amenity", AmenityImage::new, amenityImageRepo
        ));
        amenityRepo.save(amenity);
        
        log.info("Property flat added successfully with Id: {}", amenity.getId());
    }

}