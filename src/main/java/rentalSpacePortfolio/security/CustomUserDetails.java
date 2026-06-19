package rentalSpacePortfolio.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails{


    private final String userId;      // 🔑 Stores your MySQL UUID String
    private final String email;       // Used as the username for login
    private final String password;    // Stores the encrypted password
    private final Collection<? extends GrantedAuthority> authorities; // Stores roles

    // Constructor used by CustomUserDetailsService to pack the user data
    public CustomUserDetails(String userId, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // 🌟 This is the custom method your utility class was missing!
    public String getUserId() {
        return this.userId;
    }

    // --- Mandatory Spring Security UserDetails Overrides ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to true so accounts don't instantly expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set to true so accounts aren't locked out by default
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to true so credentials stay valid
    }
    
   @Override
    public boolean isEnabled() {
        return true; // Set to true so the user is active
    }

}