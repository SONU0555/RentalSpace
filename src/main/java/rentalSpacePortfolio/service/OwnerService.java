package rentalSpacePortfolio.service;

import rentalSpacePortfolio.dto.request.owner.DetailsRequest;
import rentalSpacePortfolio.dto.request.owner.ProfileUpdateRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.exception.UserNotFoundException;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.Role;
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
    
    // Service to get all admins
    public List<AdminSummaryResponse> getAllAdmins(){
        List<Admin> admins = adminRepo.findAll();
        
        return admins.stream().map(admin -> UserResponseMapper.mapUserToAdminSummaryDto(admin))
                .collect(Collectors.toList());
    } 
    
    // Service to get all tenants
    public List<TenantSummaryResponse> getAllTenants(){
        List<Tenant> tenants = tenantRepo.findAll();
        
        return tenants.stream().map(tenant -> UserResponseMapper.mapToTenatResponseDto(tenant))
                .collect(Collectors.toList());
    } 
    
    
    // Add Owner personal details
//    public void addOwnerProfileDetails(DetailsRequest request, Long id){
//        
//        User owner = userRepo.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("Wrong Id: " + id));
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
//                .orElseThrow(() -> new UserNotFoundException("Wrong owner Id: " + id));
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