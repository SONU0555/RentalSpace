package rentalSpacePortfolio.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.property.AddRequest;
import rentalSpacePortfolio.dto.request.property.PropertyImageRequest;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.PropertyImage;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.PropertyStatus;
import rentalSpacePortfolio.enums.Role;
import rentalSpacePortfolio.exception.DuplicatePropertyException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.PropertyResponseMapper;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.PropertyImageRepository;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.UserRepository;



@Service
public class PropertyService {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);
    
    private final PropertyRepository propertyRepo;
    private final AdminRepository adminRepo;
    private final UserRepository userRepo;
    private final PropertyImageRepository propertyImgRepo;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepo,
            UserRepository userRepo,
            AdminRepository adminRepo,
            PropertyImageRepository propertyImgRepo){
        this.propertyRepo = propertyRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.propertyImgRepo = propertyImgRepo;
    }
    
    // Service to fetch all properties
    public List<PropertyResponse> getAllProperties(){
        
        logger.info("Requestd to get all properties");
        List<Property> properties = propertyRepo.findAll();
        return properties.stream().map(p -> 
                PropertyResponseMapper.mapToPropertyResponse(p)).collect(Collectors.toList());
    }
    
    // Service to add new property
    @Transactional
    public void addProperty(AddRequest request, UUID ownerId){
        
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + ownerId));
        
        boolean exists = false;
        
        if(propertyRepo.existsByAddress(request.getAddress()) != null){
            exists = true;
        }
        
        if(exists){
            logger.warn("Failed to add propety: Trying to add same property twice");
            throw new DuplicatePropertyException("Property with the same address already exist in system.");
        }
        
        Property property = new Property();
        property.setName(request.getName());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPinCode(request.getPinCode());
        property.setOwner(owner);
        property.setStatus(selectPropertyStatus(request.getStatus()));
        property.setMiniumRent(request.getMinimumRent());
        property.setMaximumRent(request.getMaximumRent());
        
        propertyRepo.save(property);
        
        for(PropertyImageRequest image : request.getPropertyImages()){
        PropertyImage propertyImage = new PropertyImage();
        
            propertyImage.setImageUrl(image.getImageUrl());
            propertyImage.setIsCoverImage(image.getIsCoverImage());
            propertyImage.setDisplayOrder(image.getDisplayOrder());
            propertyImage.setProperty(property);
            
            propertyImgRepo.save(propertyImage);
        }
                
        logger.info("Property added successfully with Id: {}", property.getId());
    }
    
    // Service to update property
    
    // Method to convert String property status into enum status type
    private PropertyStatus selectPropertyStatus(String status){
     return switch (status.toUpperCase()){
            case "AVAILABLE" -> PropertyStatus.AVAILABLE;
            case "UNAVAILABLE" -> PropertyStatus.UNAVAILABLE;
            case "UNDER_MAINTENANCE" -> PropertyStatus.UNDER_MAINTENANCE;
            default -> PropertyStatus.UNAVAILABLE;
        };
    }
    
    // Service to get property by Id
    public PropertyResponse getPropertyById(UUID propertyId){
        logger.info("Requested to get property by Id {}", propertyId);
        Property property = propertyRepo.findByPropertyId(propertyId);
        
        if(property == null){
            logger.info("Can't find property due to Invalid or wrong property Id");
            throw new ResourceNotFoundException("Property not found");
        }
        
        return PropertyResponseMapper.mapToPropertyResponse(property);
    }
    
    // Service to assign specific property to an admin
    @Transactional
    public void assignPropertyToAdmin(UUID propertyId, UUID adminId, UUID ownerId){

        Property property = propertyRepo.findByPropertyId(propertyId);
        if(property == null){
           logger.warn("Property assignment failed: Property not found with id: {}", propertyId);
            throw new ResourceNotFoundException("Property not found with id: " + propertyId);
        };

        if (!property.getOwner().getId().equals(ownerId)) {
            logger.warn("Security Alert: User [{}] attempted unauthorized admin assignment on property [{}] owned by [{}]", 
                    ownerId, propertyId, property.getOwner().getId());
            throw new AccessDeniedException("You are not authorized. Only the owner can assign an admin.");
        }

        Admin admin = adminRepo.findByUserId(adminId)
                .orElseThrow(() -> {
                    logger.warn("Property assignment failed: Target admin not found with id: {}", adminId);
                    return new ResourceNotFoundException("User not found with id: " + adminId);
                });
        
        if (!admin.getAdmin().getRole().equals(Role.ADMIN)) {
            logger.warn("Property assignment failed: User [{}] does not hold an ADMIN role. Found role: {}", adminId, admin.getAdmin().getRole());
            throw new IllegalArgumentException("The assigned user must have an ADMIN role.");
        }

        logger.debug("Current admin for property [{}] is [{}]. Updating to new admin [{}].", 
                propertyId, 
                property.getAdmin() != null ? property.getAdmin().getId() : "NONE", 
                adminId);

        property.setAdmin(admin);
        admin.setProperty(property);
        propertyRepo.save(property);
        adminRepo.save(admin);

        logger.info("Successfully assigned adminId: {} to propertyId: {} by ownerId: {}", adminId, propertyId, ownerId);
    }
}