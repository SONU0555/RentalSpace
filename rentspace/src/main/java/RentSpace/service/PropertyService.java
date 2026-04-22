package RentSpace.service;

import RentSpace.entity.Property;
import RentSpace.entity.User;
import RentSpace.repository.PropertyRepository;
import RentSpace.repository.UserRepository;
import RentSpace.requestDtos.Property.AddPropertyDto;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PropertyService {
    
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
        User owner = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wrong user Id: " + id));
        
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

}