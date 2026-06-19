package rentalSpacePortfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    
    private final TenantRepository tenantRepo;
    private final AdminRepository adminRepo;
    
    @Autowired
    public UserCommonService(TenantRepository tenantRepo,
            AdminRepository adminRepo){
        this.tenantRepo = tenantRepo;
        this.adminRepo = adminRepo;
    }
    
//     Service to get tenant by Id
    public TenantSummaryResponse getTenantById(String tenantId){
        Tenant tenant = tenantRepo.findByUserId(tenantId)
                .orElseThrow(() -> new UserNotFoundException("User tenant not found with Id: " + tenantId));
        
        return UserResponseMapper.mapToTenatResponseDto(tenant);
    }
    
    // Service to get admin by id to view admin details
    public AdminSummaryResponse getAdminById(String adminId){
        Admin admin = adminRepo.findByUserId(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found with ID: " + adminId));
        
        return UserResponseMapper.mapUserToAdminSummaryDto(admin);
    }

}