package rentalSpacePortfolio.service.impl;

import java.util.List;
import rentalSpacePortfolio.dto.request.tenant.ProfileRequest;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rentalSpacePortfolio.dto.request.tenant.AadhaarRequest;
import rentalSpacePortfolio.dto.response.tenant.DashboardResponse;
import rentalSpacePortfolio.dto.response.tenant.ProfileResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.mapper.TenantResponseMapper;
import rentalSpacePortfolio.mapper.UserResponseMapper;
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
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with ID: " + tenantId));
        
        return TenantResponseMapper.mapToDashboardResponse(tenant);
    }
    
    // Service to get tenant profile
    public ProfileResponse getTenantProfile(UUID userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for tenants ID: " + userId));
        
        return TenantResponseMapper.mapToProfileResponse(user.getTenant());
    }
    
    // Service to get all tenants
    public List<TenantSummaryResponse> getAllTenants(UUID userId){
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        List<Tenant> tenants = null;
        
        if(user.getRole().toString().equals("ADMIN")){
            tenants = tenantRepo.findAllTenantByPropertyId(12l);
        }
        tenants = tenantRepo.findAll();
        
        return tenants.stream().map(tenant -> UserResponseMapper.mapToTenatResponseDto(tenant))
                .collect(Collectors.toList());
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
//            throw new ResourceNotFoundException("User not found with Id: " + id);
//        } 
//    }
    
//     Update user profile details
    @Transactional
    public void updateTenantProfile(ProfileRequest request, UUID tenantId){
        // get user if exist with this Id
        logger.info("Requested to update user profile details");
        User user = userRepo.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + tenantId));  
        
        Tenant tenant = user.getTenant();
        tenant.setEmergencyContect(request.getEmergencyContect());
        
        user.setPhone(request.getPhone());
                 
        userRepo.save(user); 
        tenantRepo.save(tenant);
    }
    
    // Verify user by a AADHAAR CARD
    @Transactional
    public void aadhaarVarification(AadhaarRequest request, UUID tenantId){
        
        logger.info("Requested to varify user via a AADHAAR CARD");
        User user = userRepo.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + tenantId));  
        
        Tenant tenant = user.getTenant();
        tenant.setAadhaarNumber(request.getAadhaarNumber());
        
        tenantRepo.save(tenant);
    }
}