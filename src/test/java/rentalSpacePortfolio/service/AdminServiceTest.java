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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import rentalSpacePortfolio.dto.response.admin.AdminSummaryResponse;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.AdminStatus;
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
    
    // Test cases for get admin by ID service
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
    
    // Test cases for get all admin service
    @Test
    void getAllAdmins_ShouldReturnMappedList_WhenDataExists(){
        int page = 0;
        int size = 10;
        
        Pageable pageable = PageRequest.of(page, size);
        
        User user1 = new User(); user1.setFull_name("Alok");
        User user2 = new User(); user1.setFull_name("Rajeev");
        
        Admin admin1 = new Admin();
        admin1.setId(UUID.randomUUID());
        admin1.setUser(user1);
        Admin admin2 = new Admin();
        admin2.setId(UUID.randomUUID());
        admin2.setUser(user2);
        
        List<Admin> adminList = List.of(admin1, admin2);
        Page<Admin> mockPage = new PageImpl<>(adminList, pageable, adminList.size());
        
        Mockito.when(adminRepo.findAll(pageable)).thenReturn(mockPage);
        
        List<AdminSummaryResponse> result = adminService.getAllAdmins(page, size);
        
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
    
    @Test
    void getAllAdmins_ShouldReturnEmptyList_WhenDataNotExits(){
        int page = 0;
        int size = 10;
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> emptyPage = Page.empty(pageable);
        
        Mockito.when(adminRepo.findAll(pageable)).thenReturn(emptyPage);
        
        List<AdminSummaryResponse> result = adminService.getAllAdmins(page, size);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
    
    @Test
    void unassignAdmin_ShouldUpdateStatusAndSave_WhenAdminIsAssigned(){
        mockAdmin.setStatus(AdminStatus.ASSIGNED);
        Mockito.when(adminRepo.findById(adminId)).thenReturn(Optional.of(mockAdmin));
        
        adminService.unassignAdmin(adminId);
        
        Assertions.assertEquals(AdminStatus.UNASSIGNED, mockAdmin.getStatus());
        Mockito.verify(adminRepo, Mockito.times(1)).save(mockAdmin);
    }
    
    @Test
    void unassignAdmin_ShouldReturnEarly_WhenAdminIsAlreadyUnassigned(){
        mockAdmin.setStatus(AdminStatus.UNASSIGNED);
        Mockito.when(adminRepo.findById(adminId)).thenReturn(Optional.of(mockAdmin));
        
        adminService.unassignAdmin(adminId);
        
        Mockito.verify(adminRepo, Mockito.never()).save(Mockito.any(Admin.class));
    }
    
    @Test
    void unassignAdmin_ShouldThrowException_WhenAdminNotExists(){        
        Mockito.when(adminRepo.findById(adminId)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            adminService.unassignAdmin(adminId);
        });
        
        Mockito.verify(adminRepo, Mockito.never()).save(Mockito.any(Admin.class));
    }

}