package rentalSpacePortfolio.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class SecurityUnits {
    
    public static String getCurrentUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getUserId();
    }
    
    public static String getCurrentUserRole(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && !auth.getAuthorities().isEmpty()){
            return auth.getAuthorities().iterator().next().getAuthority();
        }
        return null;
    }

}