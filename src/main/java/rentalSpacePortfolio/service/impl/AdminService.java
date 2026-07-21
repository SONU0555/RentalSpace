package rentalSpacePortfolio.service.impl;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.admin.ProfileResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.mapper.AdminResponseMapper;
import rentalSpacePortfolio.mapper.UserResponseMapper;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.UserRepository;



@Service
public class AdminService {
    
    public UserRepository userRepo;
    private final AdminRepository adminRepo;
    
    @Autowired
    public AdminService(UserRepository userRepo,
            AdminRepository adminRepo){
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }
    
    // Service to get admin profile
    public ProfileResponse getAdminProfile(UUID userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        return AdminResponseMapper.mapToProfileResponse(user);
    }
    
    // Service to get all admins
    public List<AdminSummaryResponse> getAllAdmins(){
        List<Admin> admins = adminRepo.findAll();
        
        return admins.stream().map(admin -> UserResponseMapper.mapUserToAdminSummaryDto(admin))
                .collect(Collectors.toList());
    } 
    
    // Service to get admin by id to view admin details
    public AdminSummaryResponse getAdminById(UUID adminId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        boolean isTenant = authentication.getAuthorities().stream().anyMatch(auth
                -> auth.getAuthority().equals("ROLE_TENANT"));
        
        Admin admin = adminRepo.findByUserId(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));
        
        if(isTenant){
            return UserResponseMapper.mapUserToAdminSummaryDtoForTenant(admin);
        }
        
        return UserResponseMapper.mapUserToAdminSummaryDto(admin);
    }
    
    
    // Delete user
    public void deleteUser(UUID id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + id));
        userRepo.delete(user);
    }

}