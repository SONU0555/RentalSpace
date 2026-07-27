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
import rentalSpacePortfolio.dto.request.flat.FlatDataRequest;
import rentalSpacePortfolio.dto.request.image.ImageRequest;
import rentalSpacePortfolio.dto.response.flat.FlatResponse;
import rentalSpacePortfolio.entity.Flat;
import rentalSpacePortfolio.entity.FlatImage;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.enums.FlatStatus;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.FlatMapper;
import rentalSpacePortfolio.mapper.FlatResponseMapper;
import rentalSpacePortfolio.repository.FlatImageRepository;
import rentalSpacePortfolio.repository.FlatRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;

@Slf4j
@Service
public class FlatService {
    
    private final PropertyRepository propertyRepo;
    private final FlatRepository flatRepo;
    private final FlatImageRepository flatImageRepo;
    private final ImageStorageService imageStorageService;
    private final CommonService commonService;
    
    @Autowired
    public FlatService(
            PropertyRepository propertyRepo,
            FlatRepository flatRepo,
            ImageStorageService imageStorageService,
            FlatImageRepository flatImageRepo,
            CommonService commonService){
        this.propertyRepo = propertyRepo;
        this.flatRepo = flatRepo;
        this.imageStorageService = imageStorageService;
        this.flatImageRepo = flatImageRepo;
        this.commonService = commonService;
    }
    
    // Service to get all flat of specific property by Id
    public List<FlatResponse> getPropertyAllFlat(UUID propertyId, int page, int size){
        propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                    log.info("Faile to fetch Flats! property not found with Id: {}", propertyId);
                    return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });
        
        log.info("fetching data for db for page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Flat> flats = flatRepo.findAllWithImages(propertyId, pageable);
        return flats.stream().map(flat -> FlatResponseMapper.mapToFlatDto(flat)).collect(Collectors.toList());
    }
    
    
    
    // Service to add flat in property
    @Transactional
    public void saveFlat(List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            FlatDataRequest flatData,
            UUID propertyId) throws IOException{
        
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow( () -> {
                        log.info("Failed to add Flat! property not found with Id: {}", propertyId);
                        return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                }
        );
        
        Flat flat = new Flat();
        FlatMapper.mapToFlatDto(flat, flatData, property);
        Flat savedFlat = flatRepo.save(flat);
        log.info("Flat data added successfully, now adding images");
        
        savedFlat.setFlatImages(commonService.mapAndSaveImage(
                images, imageDetails, flat, "flat", FlatImage::new, flatImageRepo
        ));
        flatRepo.save(savedFlat);
        
        log.info("Property flat added successfully with Id: {}", savedFlat.getId());
        
    }
    
    // service to update property flat
    @Transactional
    public void updateFlat(
            List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            FlatDataRequest flatData,
            UUID flatId
    ) throws IOException{
        
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + flatId));
        
        List<FlatImage> flatImages = flatImageRepo.findAllImagesByFlatId(flatId);
        
        List<MultipartFile> newImages = new ArrayList<>();
        List<ImageRequest> newImageDetails = new ArrayList<>();
        
        for(ImageRequest details : imageDetails){
            if(details.getId() == null){
                FlatImage image = flatImageRepo.findImageByDisplayOrder(flat.getId(), details.getDisplayOrder());
                if(image != null){
                    flatImageRepo.delete(image);
                    log.info("Existing image from place: {} removed form DB and disk to store new image at same place", 
                        details.getDisplayOrder());
                    
                    log.info("Removing image file from local disk");
                    imageStorageService.delete(image.getImageDetails().getImageUrl());
                }
                                
                newImages.add(images.get(details.getDisplayOrder() - 1));
                newImageDetails.add(details);          
            }else{
                boolean exists = flatImages.stream()
                     .anyMatch(image -> details.getId().equals(image.getId()));

                if (!exists) {
                   log.warn("Update image failed! uploaded image Id: {} does not belongs to flat images", details.getId());
                   throw new ResourceNotFoundException("Image not found with Id: " + details.getId());
                }
            }
        }
        
        log.info("Mapping property to dto and saving to database");
        FlatMapper.mapToFlatDto(flat, flatData, null);
        Flat updatedFlat = flatRepo.save(flat);
        
        log.info("Updating images in DB and local disk, Setting image to saved flat: {}", updatedFlat.getId());
        updatedFlat.setFlatImages(commonService.mapAndSaveImage(
                newImages, newImageDetails, flat, "flat", FlatImage::new, flatImageRepo
        ));
        flatRepo.save(updatedFlat);
        
        log.info("Flat image and data successfully updated with Id: {}", updatedFlat.getId());
    }
    
    // Service to set active and In-Active flat
    public void softDelete(UUID flatId, boolean isDeleted){
        log.info("Requested to deactivate flat by Id {}", flatId);
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> {
                     log.warn("Soft deletion failed: flat not found with id: {}", flatId);
                     return new ResourceNotFoundException("flat not found with Id: " + flatId);
                });
        
        flat.setDeleted(isDeleted);
        flatRepo.save(flat);
        log.info("flat: {} succefully deleted softly", flatId);
    }

}