package RentSpace.service;

import RentSpace.entity.User;
import RentSpace.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            
            return org.springframework.security.core.userdetails.User.builder()
                   .username(isUserExist.getEmail())
                   .password(isUserExist.getPassword())
                   .roles(isUserExist.getRole().name())
                   .build();

    }

}