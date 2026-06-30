package rentalSpacePortfolio.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import rentalSpacePortfolio.entity.User;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.response.user.ProfileResponse;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.mapper.UserResponseMapper;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;


@Service
public class OwnerService {
    
    public final UserRepository userRepo;
    public final AdminRepository adminRepo;
    public final TenantRepository tenantRepo;
    
    @Autowired
    public OwnerService(UserRepository userRepo,
            AdminRepository adminRepo,
            TenantRepository tenantRepo){
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.tenantRepo = tenantRepo;
    }
    
    // Service to get profile
    public ProfileResponse getOwnerProfile(UUID userId){
        User user = userRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return UserResponseMapper.mapToOwnerProfile(user);
    }
    
    // Add Owner personal details
//    public void addOwnerProfileDetails(DetailsRequest request, Long id){
//        
//        User owner = userRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Wrong Id: " + id));
//        
//            owner.setPhone(request.getPhone());
//            owner.setAddress(request.getAddress());
//            owner.setAadhaarNumber(request.getAadhaarNumber());
//            owner.setEmergencyContect(request.getEmergencyContect());
//            owner.setCompanyName(request.getCompanyName());
//            owner.setUpdatedDate(LocalDateTime.now());
//            userRepo.save(owner);            
//
//    }    
    
    // Update Owner profile details
//    public void updateOwnerProfile(ProfileUpdateRequest request, Long id){
//        User owner = userRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Wrong owner Id: " + id));
//        
//            owner.setFull_name(request.getName());
//            owner.setEmail(request.getEmail());
//            owner.setPhone(request.getPhone());
//            owner.setAddress(request.getAddress());
//            owner.setAadhaarNumber(request.getAadhaarNumber());
//            owner.setEmergencyContect(request.getEmergencyContect()); 
//            owner.setCompanyName(request.getCompanyName());
//            owner.setUpdatedDate(LocalDateTime.now());
//                 
//            userRepo.save(owner);
//    }

}