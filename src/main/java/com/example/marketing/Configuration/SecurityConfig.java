package com.example.marketing.Configuration;

import com.example.marketing.model.Role;
import com.example.marketing.model.User;
import com.example.marketing.repository.RoleRepository;
import com.example.marketing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults; 

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/analysis/**").permitAll() 
                // .requestMatchers("/swagger-ui/**").permitAll()
                // .requestMatchers("/swagger-ui.html").permitAll()
                // .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            
            
            .httpBasic(withDefaults())
            
            .authenticationProvider(authenticationProvider()) 
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            .logout(logout -> logout.logoutUrl("/api/v1/auth/logout").permitAll());
        
        return http.build();
    }



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); 
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            
            Role userRole;
            if (roleRepository.findByRoleName("USER").isEmpty()) {
                userRole = new Role();
                userRole.setRoleName("USER");
                roleRepository.save(userRole);
            } else {
                userRole = roleRepository.findByRoleName("USER").get();
            }

            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setEmail("user@example.com");
                testUser.setPasswordHash(passwordEncoder.encode("password123")); 
                testUser.setActive(true);
                testUser.setRole(userRole); 
                
                userRepository.save(testUser);
                System.out.println(">>> Usuario de prueba 'user@example.com' (pass: 'password123') creado <<<");
            }
        };
    }
}