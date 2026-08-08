    package rentalSpacePortfolio.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.request.amenity.AmenityRequest;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.response.Amenity.AmenityResponse;
import rentalSpacePortfolio.entity.Amenity;
import rentalSpacePortfolio.entity.AmenityImage;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.enums.AmenityStatus;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.AmenityMapper;
import rentalSpacePortfolio.repository.AmenityImageRepository;
import rentalSpacePortfolio.repository.AmenityRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;


@Slf4j
@Service
public class AmenityService {
    
    private final AmenityRepository amenityRepo;
    private final PropertyRepository propertyRepo;
    private final AmenityImageRepository amenityImageRepo;
    private final CommonService commonService;
    private final ImageStorageService imageStorageService;
    
    @Autowired
    public AmenityService(
            AmenityRepository amenityRepo,
            PropertyRepository propertyRepo,
            AmenityImageRepository amenityImageRepo,
            CommonService commonService,
            ImageStorageService imageStorageService){
        this.amenityRepo = amenityRepo;
        this.propertyRepo = propertyRepo;
        this.amenityImageRepo = amenityImageRepo;
        this.commonService = commonService;
        this.imageStorageService = imageStorageService;
    }
    
    // Service to get all flat of specific property by Id
    public List<AmenityResponse> getPropertyAllAmenity(UUID propertyId, int page, int size){
        propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                    log.info("Faile to fetch Flats! property not found with Id: {}", propertyId);
                    return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });
        
        log.info("fetching data for db for page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Amenity> amenities = amenityRepo.findAllWithImages(propertyId, pageable);
        return amenities.stream().map(amenity -> AmenityMapper.mapToAmenityResponseDto(amenity)).collect(Collectors.toList());
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
        Amenity savedAmenity = amenityRepo.save(amenity);
        log.info("Amenity data added successfully, now adding images");
        
        amenity.setAmenityImages(commonService.mapAndSaveImage(
                images, imageDetails, savedAmenity, "amenity", AmenityImage::new, amenityImageRepo
        ));
        amenityRepo.save(savedAmenity);
        
        log.info("Property flat added successfully with Id: {}", savedAmenity.getId());
    }
    
    // service to update property flat
    @Transactional
    public void updateAmenity(
            List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            AmenityRequest amenityData,
            UUID amenityId
    ) throws IOException{
        
        Amenity amenity = amenityRepo.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + amenityId));
        
        List<AmenityImage> amenityImages = amenityImageRepo.findAllImagesByAmenityId(amenityId);
        
        List<MultipartFile> newImages = new ArrayList<>();
        List<ImageRequest> newImageDetails = new ArrayList<>();
        
        for(ImageRequest details : imageDetails){
            if(details.getId() == null){
                // find image in DB whether the given image deatils diplay-order exists in amenity image table or not 
                AmenityImage image = amenityImageRepo.findImageByDisplayOrder(amenity.getId(), details.getDisplayOrder());
                if(image != null){
                    amenityImageRepo.delete(image);
                    log.info("Existing image from place: {} removed form DB and disk to store new image at same place", 
                        details.getDisplayOrder());
                    
                    log.info("Removing image file from local disk");
                    imageStorageService.delete(image.getImageDetails().getImageUrl());
                }
                                
                newImages.add(images.get(details.getDisplayOrder() - 1));
                newImageDetails.add(details);          
            }else{
                boolean exists = amenityImages.stream() // Check given image ID is exist in existing image list or not
                     .anyMatch(image -> details.getId().equals(image.getId()));

                if (!exists) {
                   log.warn("Update image failed! uploaded image Id: {} does not belongs to Amenity images", details.getId());
                   throw new ResourceNotFoundException("Image not found with Id: " + details.getId());
                }
            }
        }
        
        log.info("Mapping amenity to dto and saving to database");
        AmenityMapper.mapToAmenityDto(amenity, amenityData, null);
        amenityRepo.save(amenity);
        
        log.info("Updating images in DB and local disk, Setting image to saved amenity: {}", amenity.getId());
        amenity.setAmenityImages(commonService.mapAndSaveImage(
                newImages, newImageDetails, amenity, "amenity", AmenityImage::new, amenityImageRepo
        ));
        amenityRepo.save(amenity);
        
        log.info("Amenity image and data successfully updated with Id: {}", amenity.getId());
    }
    
    // Service to set active and In-Active Amenity
    public void softDelete(UUID amenityId, boolean isDeleted){
        log.info("Requested to deactivate flat by Id {}", amenityId);
        Amenity amenity = amenityRepo.findById(amenityId)
                .orElseThrow(() -> {
                     log.warn("Soft deletion failed: amenity not found with id: {}", amenityId);
                     return new ResourceNotFoundException("amenity not found with Id: " + amenityId);
                });
        
        amenity.setStatus(AmenityStatus.INACTIVE);
        amenityRepo.save(amenity);
        log.info("amenity: {} succefully deleted softly", amenityId);
    }

}