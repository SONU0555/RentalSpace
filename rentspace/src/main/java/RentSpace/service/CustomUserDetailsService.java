package RentSpace.service;

import RentSpace.entity.User;
import RentSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    private final UserRepository userRepo;
    
    @Autowired
    public CustomUserDetailsService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User isUserExist = userRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            
            return org.springframework.security.core.userdetails.User.builder()
                    .username(isUserExist.getEmail()).password(isUserExist.getPassword())
                    .roles(isUserExist.getRole().name()).build();
    }

}