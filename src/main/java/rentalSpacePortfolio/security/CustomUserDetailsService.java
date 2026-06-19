package rentalSpacePortfolio.security;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rentalSpacePortfolio.entity.User;
import rentalSpacePortfolio.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    public static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepo;
    
    @Autowired
    public CustomUserDetailsService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User isUserExist = userRepo.findByEmail(email);
            
            if(isUserExist == null){
                logger.warn("Authentication failed: User not found with email: {}", email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            
            List<SimpleGrantedAuthority> authorities = List.of(
                 new SimpleGrantedAuthority("ROLE_" + isUserExist.getRole().name())
            );
            
            return new CustomUserDetails(
                    isUserExist.getId().toString(),
                    isUserExist.getEmail(),
                    isUserExist.getPassword(),
                    authorities
            );
           
    }

}