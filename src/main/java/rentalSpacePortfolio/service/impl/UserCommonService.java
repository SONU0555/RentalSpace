package rentalSpacePortfolio.service.impl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.dto.response.tenant.TenantSummaryResponse;
import rentalSpacePortfolio.entity.Tenant;
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
                .orElseThrow(() -> new ResourceNotFoundException("User tenant not found with Id: " + tenantId));
        
        return UserResponseMapper.mapToTenatResponseDto(tenant);
    }
}