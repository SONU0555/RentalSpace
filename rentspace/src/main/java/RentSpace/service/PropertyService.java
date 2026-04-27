package RentSpace.service;

import RentSpace.Exception.PropertyNotFoundException;
import RentSpace.Exception.UserNotFoundException;
import RentSpace.entity.Property;
import RentSpace.entity.User;
import RentSpace.entityResponseMapper.PropertyResponseMapper;
import RentSpace.repository.PropertyRepository;
import RentSpace.repository.UserRepository;
import RentSpace.requestDtos.Property.AddPropertyDto;
import RentSpace.responseDto.PropertyResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {
    
    public static final Logger logger = LoggerFactory.getLogger(PropertyService.class);
    
    private final PropertyRepository propertyRepo;
    private final UserRepository userRepo;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepo, UserRepository userRepo){
        this.propertyRepo = propertyRepo;
        this.userRepo = userRepo;
    }
    
    // Add new property service
    @Transactional
    public void addNewProperty(AddPropertyDto request, Long id){
        
        logger.info("Processing of adding new property with owner Id: {}", id);
        
        User owner = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Wrong user Id: " + id));
        
        
        Property property = new Property();
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setPrice(request.getPrice());
        property.setStatus(Property.Status.AVAILABLE);
        property.setOwner(owner);
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        owner.getProperties().add(property);
        
        propertyRepo.save(property);
    }
    
    // Update property details
    public void updateProperty(AddPropertyDto request, Long propertyId, Long ownerId){
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with Id: " + propertyId));
        
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + ownerId));
        
        if(property.getOwner().getId().equals(ownerId)){
            logger.info("Updating property details");
            
           property.setTitle(request.getTitle());
           property.setDescription(request.getDescription());
           property.setLocation(request.getLocation());
           property.setPrice(request.getPrice());
           property.setStatus(request.getStatus().equalsIgnoreCase("rented") ? Property.Status.RENTED : Property.Status.AVAILABLE);
           property.setUpdatedAt(LocalDateTime.now());
        
           propertyRepo.save(property);
        }else{
            logger.warn("update failed: requested property Id: {} doesn't belongs to owner Id : {}");
        }
    }
    
    // Delete Property 
    public void deleteOwnerProperty(Long ownerId, Long propertyId){
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + ownerId));
        
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with Id: " + propertyId));
        
        if(property.getOwner().getId().equals(ownerId)){
            propertyRepo.delete(property);
        }else{
            logger.warn("delete failed: requested property Id: {} doesn't belongs to owner Id : {}");
        }
    }
    
    // get all properties of specific owner
    public List<PropertyResponseDto> getPropertiesOfSingleOwner(Long ownerId){
        User user = userRepo.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + ownerId));
        
        List<PropertyResponseDto> properties = user.getProperties().stream().map(p -> 
                                               PropertyResponseMapper.mapToPropertyResponse(p)).toList();
        
        return properties;
    } 
    
    // get all properties of all owners
    public List<PropertyResponseDto> getPropertiesOfAllOwners(){
        
        List<PropertyResponseDto> properties = propertyRepo.findAll().stream().map(p -> 
                                               PropertyResponseMapper.mapToPropertyResponse(p)).toList();
        
        return properties;
    } 

}