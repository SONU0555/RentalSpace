package rentalSpacePortfolio.service;

import rentalSpacePortfolio.dto.request.tenant.ProfileUpdateRequest;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.request.tenant.ChangePasswordRequest;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.exception.UserNotFoundException;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.mapper.TenantResponseMapper;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;


@Service
public class TenantService {
    
    private final static Logger logger = LoggerFactory.getLogger(TenantService.class);
    
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    
    @Autowired
    public TenantService(UserRepository userRepo,
            TenantRepository tenantRepo){
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
    }
    
    // Service to get tenant Dashboard
    public DashboardResponse getTenantDashboard(UUID tenantId){
        Tenant tenant = tenantRepo.findByUserId(tenantId)
                .orElseThrow(() -> new UserNotFoundException("Tenant not found with ID: " + tenantId));
        
        return TenantResponseMapper.mapToDashboardResponse(tenant);
    }
    
    // Service to get tenant profile
    public ProfileResponse getTenantProfile(UUID userId){
        User user = userRepo.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for tenants ID: " + userId));
        
        return TenantResponseMapper.mapToProfileResponse(user.getTenant());
    }
    
    // Add user personal details
//    public void addTenantProfile(DetailsRequest request, Long id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepo.findByEmail(authentication.getName());
//        
//        
//        if(user.getId().equals(id)){
//            user.setPhone(request.getPhone());
//            user.setAddress(request.getAddress());
//            user.setRentStartDate(LocalDate.now());
//            user.setRentEndDate(request.getRentEndDate());
//            user.setAadhaarNumber(request.getAadhaarNumber());
//            user.setUpdatedDate(LocalDateTime.now());
//            user.setEmergencyContect(request.getEmergencyContect());
//
//            userRepo.save(user);            
//        }else{
//            throw new UserNotFoundException("User not found with Id: " + id);
//        } 
//    }
    
//     Update user profile
    public void updateTenantProfile(ProfileUpdateRequest request, UUID tenantId){
        // get user if exist with this Id
        
        logger.info("Processing of updating user profile");
        
        User tenant = userRepo.findByUserId(tenantId)
                .orElseThrow(() -> new UserNotFoundException("Wrong user Id: " + tenantId));  
        
           request.getFull_name().ifPresent(tenant::setFull_name);
           request.getEmail().ifPresent(tenant::setEmail);
           request.getPhone().ifPresent(tenant::setPhone);
                 
            userRepo.save(tenant); 
    }
    
}