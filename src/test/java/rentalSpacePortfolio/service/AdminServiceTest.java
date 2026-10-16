package rentalSpacePortfolio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.exception.ResourceNotFoundException;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.UserRepository;
import rentalSpacePortfolio.service.impl.AdminService;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @Mock private AdminRepository adminRepo;
    @Mock private UserRepository userRepo;
        
    @InjectMocks
    private AdminService adminService;
    
    private UUID adminId;
    private Admin mockAdmin;
    
    private UUID adminUserId;
    private User mockAdminUser;
    
    @BeforeEach
    void setUp() {
        adminId = UUID.randomUUID();
        mockAdmin = new Admin();
        
        adminUserId = UUID.randomUUID();
        mockAdminUser = new User();
        mockAdminUser.setFull_name("Hitesh");
        mockAdminUser.setEmail("hitesh@gmail.com");
        mockAdminUser.setPhone("7728277322");
        mockAdminUser.setCreatedAt(LocalDateTime.MAX);
        mockAdminUser.setUpdatedAt(LocalDateTime.MIN);
        
        mockAdmin.setId(adminId);
        mockAdmin.setEmployeeCode("EMP1212");
        mockAdmin.setUser(mockAdminUser);
    }
    
    
    @Test
    void getAdminProfile_ShouldThrowException_WhenUserNotExist(){
        Mockito.when(userRepo.findById(adminUserId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            adminService.getAdminProfile(adminUserId);
        });
    }
    
    @Test
    void getAdminById_ForTenantUser(){
        Mockito.when(adminRepo.findById(adminId)).thenReturn(Optional.of(mockAdmin));
        
        // mock spring security Context for a Tenant
        Authentication authentication = Mockito.mock(Authentication.class);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TENANT");
        Mockito.doReturn(List.of(authority)).when(authentication).getAuthorities();
        
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        AdminSummaryResponse res = adminService.getAdminById(adminId);
        
        Assertions.assertNotNull(res);
        Assertions.assertFalse(res.getEmployeeCode().isEmpty());
    }
    
    @Test
    void shouldThrowException_WhenAdminDoesNotExist(){
        Mockito.when(adminRepo.findById(adminId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            adminService.getAdminById(adminId);
        });
    }

}