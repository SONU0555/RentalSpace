package rentalSpacePortfolio.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.property.AddRequest;
import rentalSpacePortfolio.dto.response.property.PropertyResponse;
import rentalSpacePortfolio.entity.Property;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.PropertyStatus;
import rentalSpacePortfolio.exception.DuplicatePropertyException;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.PropertyResponseMapper;
import rentalSpacePortfolio.repository.PropertyRepository;
import rentalSpacePortfolio.repository.UserRepository;



@Service
public class PropertyService {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);
    
    private final PropertyRepository propertyRepo;
    private final UserRepository userRepo;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepo,
            UserRepository userRepo){
        this.propertyRepo = propertyRepo;
        this.userRepo = userRepo;
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
    public void addProperty(AddRequest request){
        
        boolean exists = false;
        
        if(propertyRepo.existsByAddress(request.getAddress()) != null){
            exists = true;
        }
        
        if(exists){
            throw new DuplicatePropertyException("Property with the same address already exist in system.");
        }
        
        Property property = new Property();
        property.setName(request.getName());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setPinCode(request.getPinCode());
        property.setStatus(selectPropertyStatus(request.getStatus()));
        property.setCoverImage(request.getCoverImage());
        property.setMiniumRent(request.getMinimumRent());
        property.setMaximumRent(request.getMaximumRent());
        
        propertyRepo.save(property);
    }
    
    // Method to convert String property status into enum status type
    private PropertyStatus selectPropertyStatus(String status){
     return switch (status.toUpperCase()){
            case "AVAILABLE" -> PropertyStatus.AVAILABLE;
            case "UNAVAILABLE" -> PropertyStatus.UNAVAILABLE;
            case "UNDER_MAINTENANCE" -> PropertyStatus.UNDER_MAINTENANCE;
            default -> PropertyStatus.UNAVAILABLE;
        };
    }
    
    public PropertyResponse getPropertyById(UUID propertyId){
        logger.info("Requested to get property by Id {}", propertyId);
        Property property = propertyRepo.findByPropertyId(propertyId);
        
        if(property == null){
            logger.info("Can't find property due to Invalid or wrong property Id");
            throw new ResourceNotFoundException("Property not found");
        }
        
        return PropertyResponseMapper.mapToPropertyResponse(property);
    }

}