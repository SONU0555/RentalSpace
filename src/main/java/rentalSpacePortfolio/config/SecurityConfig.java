package rentalSpacePortfolio.config;


import rentalSpacePortfolio.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rentalSpacePortfolio.constants.ApiPaths;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final CustomUserDetailsService myUserDetails;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public SecurityConfig(CustomUserDetailsService myUserDetails,
            PasswordEncoder passwordEncoder){
        this.myUserDetails = myUserDetails;
        this.passwordEncoder = passwordEncoder;
    }
    
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(myUserDetails);
        provider.setPasswordEncoder(passwordEncoder);
                
        return provider;
    }

    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ApiPaths.BASE + "/auth/**",
                                ApiPaths.BASE +  "/public/**", ApiPaths.BASE + "/payments/webhook/**").permitAll()
                        .requestMatchers("/api/owner/**", ApiPaths.BASE + "/owner/properties/**").hasRole("OWNER")
                        .anyRequest().authenticated()
                )
                        .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}