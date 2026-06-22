package rentalSpacePortfolio.auth;


import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rentalSpacePortfolio.dto.request.auth.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.dto.request.user.UpdateCredentailsRequest;
import rentalSpacePortfolio.dto.request.auth.RegisterRequest;
import rentalSpacePortfolio.dto.request.tenant.ChangePasswordRequest;
import rentalSpacePortfolio.entity.Admin;
import rentalSpacePortfolio.entity.Tenant;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.enums.Role;
import rentalSpacePortfolio.exception.UserNotFoundException;
import rentalSpacePortfolio.repository.AdminRepository;
import rentalSpacePortfolio.repository.TenantRepository;
import rentalSpacePortfolio.repository.UserRepository;


@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final TenantRepository tenantRepo;
    public PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthService(UserRepository userRepo,
            AdminRepository adminRepo,
            TenantRepository tenantRepo,
            PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.tenantRepo = tenantRepo;
        this.passwordEncoder = passwordEncoder;
    }
    
//     Password Encoder use BCrytp
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    // Service to register new user tenant or admin
    public void createNewUser(RegisterRequest request, String role){
        
        boolean isOwnerExist = false;
        
        // Prevent to create duplicate owner
        if(role.equalsIgnoreCase("OWNER")){
            User owner = userRepo.findOwnerByRole(Role.OWNER);
            if(owner != null){
                isOwnerExist = true;
            }
        }
        
        if(isOwnerExist){
            throw new RuntimeException("Duplicate owner creation exception");
        }
        
        Admin admin = null;
        Tenant tenant = null;
        
        if(role.equalsIgnoreCase("ADMIN")){
            admin = new Admin();
        }else if(role.equalsIgnoreCase("TENANT")){
            tenant = new Tenant();
        }
        
        User user = new User();
                
        //Mapping Dto to Entity
        user.setFull_name(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder().encode(request.getPassword()));
        user.setRole(switch (role.toUpperCase()) {
                      case "ADMIN"  -> Role.ADMIN;
                      case "OWNER"  -> Role.OWNER;
                      default       -> Role.TENANT;
        });
        
        userRepo.save(user);
        
        // To store the reference key of user table in admin or tenant table
        if(admin != null){
            admin.setAdmin(user);
            adminRepo.save(admin);
        }
        if(tenant != null){
            tenant.setUser(user);
            tenantRepo.save(tenant);
        }
    }
    
    // Login back existing user
    public String login(LoginRequest request){
        User user = userRepo.findByEmail(request.getEmail());
        
        if(passwordEncoder().matches(request.getPassword(), user.getPassword())){
            return  "LoggedIn success user: " + user.getFull_name();
        }else{
            throw new RuntimeException("Wrong password");
        }
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

    
    // Update Credentails of the user
    public void updateCredential(UpdateCredentailsRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(authentication.getName());
        
            if(passwordEncoder().matches(request.getOldPassword(), user.getPassword())){
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder().encode(request.getNewPassword()));
                userRepo.save(user);
            }else{
                throw new RuntimeException("Old password missmatch");
            }
        } 

}