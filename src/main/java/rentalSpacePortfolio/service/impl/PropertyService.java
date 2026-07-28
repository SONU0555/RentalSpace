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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rentalSpacePortfolio.dto.image.ImageRequest;
import rentalSpacePortfolio.dto.request.property.PropertyRequest;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.entity.*;
import rentalSpacePortfolio.enums.PropertyStatus;
import rentalSpacePortfolio.enums.PropertyTier;
import rentalSpacePortfolio.enums.PropertyVisbility;
import rentalSpacePortfolio.enums.Role;
import rentalSpacePortfolio.exception.DuplicatePropertyException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.PropertyMapper;
import rentalSpacePortfolio.mapper.PropertyResponseMapper;
import rentalSpacePortfolio.repository.*;
import rentalSpacePortfolio.service.interfaces.ImageStorageService;


@Slf4j
@Service
public class PropertyService {
        
    private final PropertyRepository propertyRepo;
    private final AdminRepository adminRepo;
    private final UserRepository userRepo;
    private final PropertyImageRepository propertyImgRepo;
    private final ImageStorageService imageStorageService;
    private final CommonService commonService;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepo,
            UserRepository userRepo,
            AdminRepository adminRepo,
            PropertyImageRepository propertyImgRepo,
            ImageStorageService imageStorageService,
            CommonService commonService){
        this.propertyRepo = propertyRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.propertyImgRepo = propertyImgRepo;
        this.imageStorageService = imageStorageService;
        this.commonService = commonService;
    }
    
    // Service to fetch all properties
    public List<PropertyResponse> getAllProperties(int page, int size){
        
        log.info("Requestd to get all properties");
        Pageable pageable = PageRequest.of(page, size);
        Page<Property> propertyPage = propertyRepo.findAllWithImages(pageable);
        
        return propertyPage.stream().map(p -> 
                PropertyResponseMapper.mapToPropertyResponse(p)).collect(Collectors.toList());
    }
    
    // Service to save new property
    @Transactional
    public void createProperty(List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            PropertyRequest propertyData, UUID ownerId) throws IOException{
        
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + ownerId));
        
        log.info("Validating is the same property already exists");
        
        if(propertyRepo.existsByAddress(propertyData.getAddress())){
            log.warn("Failed to add propety: Trying to add same property twice");
            throw new DuplicatePropertyException("Property with the same address already exist in system.");
        }
        
        Property property = new Property();
       PropertyMapper.mapToPropertyDto(property, propertyData, owner);
        
        Property savedProperty = propertyRepo.save(property);
        log.info("Property data successfully added with Id: {}", savedProperty.getId());
                
        // save the list of images FK references in property
        savedProperty.setPropertyImages(commonService.mapAndSaveImage(
                images, imageDetails, property, "property", PropertyImage::new, propertyImgRepo
        ));
        savedProperty.setVisibility(PropertyVisbility.DRAFT);
        propertyRepo.save(savedProperty);
                
        log.info("Property added successfully with Id: {}", property.getId());
    }
    
    // Service to update property details
    @Transactional
    public void updateProperty(
            List<MultipartFile> images, 
            List<ImageRequest> imageDetails, 
            PropertyRequest propertyData,
            UUID propertyId,
            UUID ownerId) throws IOException{
        
        userRepo.findById(ownerId)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("User not found with Id: " + ownerId);
                });
        
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + propertyId));
        
        log.info("Owner with Id: {} requested to update property: {} details", ownerId, propertyId);
        
        List<PropertyImage> propertyImages = propertyImgRepo.findAllImagesByPropertyId(propertyId);
        
        List<MultipartFile> newImages = new ArrayList<>();
        List<ImageRequest> newImageDetails = new ArrayList<>();
        
        for(ImageRequest details : imageDetails){
            if(details.getId() == null){
                PropertyImage image = propertyImgRepo.findImageByDisplayOrder(property.getId(), details.getDisplayOrder());
                if(image != null){
                    propertyImgRepo.delete(image);
                    log.info("Existing image from place: {} removed form DB and disk to store new image at same place", 
                        details.getDisplayOrder());
                    
                    log.info("Removing image file from local disk");
                    imageStorageService.delete(image.getImageDetails().getImageUrl());
                }
                                
                newImages.add(images.get(details.getDisplayOrder() - 1));
                newImageDetails.add(details);          
            }else{
                boolean exists = propertyImages.stream()
                     .anyMatch(image -> details.getId().equals(image.getId()));

                if (!exists) {
                   log.warn("Update image failed! uploaded image Id: {} does not belongs to property images", details.getId());
                   throw new ResourceNotFoundException("Image not found with Id: " + details.getId());
                }
            }
        }
        
        log.info("Mapping property to dto and saving to database");
        PropertyMapper.mapToPropertyDto(property, propertyData, null);
        propertyRepo.save(property);
        
        log.info("Updating images in DB and local disk, Setting image to saved property: {}", property.getId());
        property.setPropertyImages(commonService.mapAndSaveImage(
                newImages, newImageDetails, property, "property", PropertyImage::new, propertyImgRepo
        ));
        propertyRepo.save(property);
        
        log.info("Property image and data successfully updated with Id: {}", property.getId());
    }
    
    // Service to get property by Id
    public PropertyResponse getPropertyById(UUID propertyId){
        log.info("Requested to get property by Id {}", propertyId);
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                     log.warn("Property assignment failed: Property not found with id: {}", propertyId);
                     return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });
        
        return PropertyResponseMapper.mapToPropertyResponse(property);
    }
    
    // Service to activate and deactivate property
    public void softDelete(UUID propertyId, boolean isActive){
        log.info("Requested to soft delete property by Id {}", propertyId);
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                     log.warn("Soft deletion failed: Property not found with id: {}", propertyId);
                     return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });
        
        property.setActive(isActive);
        propertyRepo.save(property);
        log.info("Property: {} succefully deactivated", propertyId);
    }
    
    // Service to assign specific property to an admin
    @Transactional
    public void assignPropertyToAdmin(UUID propertyId, UUID adminId, UUID ownerId){

        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> {
                    log.warn("Property assignment failed: Property not found with id: {}", propertyId);
                     return new ResourceNotFoundException("Property not found with Id: " + propertyId);
                });

        if (!property.getOwner().getId().equals(ownerId)) {
            log.warn("Security Alert: User [{}] attempted unauthorized admin assignment on property [{}] owned by [{}]", 
                    ownerId, propertyId, property.getOwner().getId());
            throw new AccessDeniedException("You are not authorized. Only the owner can assign an admin.");
        }

        Admin admin = adminRepo.findByUserId(adminId)
                .orElseThrow(() -> {
                    log.warn("Property assignment failed: Target admin not found with id: {}", adminId);
                    return new ResourceNotFoundException("User not found with id: " + adminId);
                });
        
        if (!admin.getAdmin().getRole().equals(Role.ADMIN)) {
            log.warn("Property assignment failed: User [{}] does not hold an ADMIN role. Found role: {}", adminId, admin.getAdmin().getRole());
            throw new IllegalArgumentException("The assigned user must have an ADMIN role.");
        }

        log.debug("Current admin for property [{}] is [{}]. Updating to new admin [{}].", 
                propertyId, 
                property.getAdmin() != null ? property.getAdmin().getId() : "NONE", 
                adminId);

        property.setAdmin(admin);
        admin.setProperty(property);
        propertyRepo.save(property);
        adminRepo.save(admin);

        log.info("Successfully assigned adminId: {} to propertyId: {} by ownerId: {}", adminId, propertyId, ownerId);
    }
}