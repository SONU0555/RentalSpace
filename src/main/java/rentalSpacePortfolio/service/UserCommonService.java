package rentalSpacePortfolio.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.request.tenant.ChangePasswordRequest;
import rentalSpacePortfolio.exception.UserNotFoundException;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.mapper.UserResponseMapper;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;


@Service
public class UserCommonService {
    
    private final static Logger logger = LoggerFactory.getLogger(UserCommonService.class);
    
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final AdminRepository adminRepo;
    public final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserCommonService(UserRepository userRepo,
            TenantRepository tenantRepo,
            AdminRepository adminRepo,
            PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }
    
//     Service to get tenant by Id
    public TenantSummaryResponse getTenantById(UUID tenantId){
        Tenant tenant = tenantRepo.findByUserId(tenantId)
                .orElseThrow(() -> new UserNotFoundException("User tenant not found with Id: " + tenantId));
        
        return UserResponseMapper.mapToTenatResponseDto(tenant);
    }
    
    // Service to get admin by id to view admin details
    public AdminSummaryResponse getAdminById(UUID adminId){
        Admin admin = adminRepo.findByUserId(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found with ID: " + adminId));
        
        return UserResponseMapper.mapUserToAdminSummaryDto(admin);
    }
    
    // Change password credential of the user
    public void changeTenantPassword(ChangePasswordRequest request, UUID userId){
        
        logger.info("Changing tenant password credentials");

           User user = userRepo.findByUserId(userId)
                   .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));
           
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
              throw new IllegalArgumentException("Current password is incorrect");
        }
        if(user.getPassword().equals(request.getNewPassword())){
            throw new IllegalArgumentException("New password must be differnt form current password");
        }
        if(!request.getConfirmPassword().equals(request.getNewPassword())){
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        
        user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        userRepo.save(user);
    }

}