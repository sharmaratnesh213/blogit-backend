package com.blogit.config;

import com.blogit.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(customizer -> customizer.disable())  // CSRF protection is disabled
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/public/**").permitAll()  // Public routes
                .requestMatchers("/api/auth/**").permitAll()  // Authentication routes
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Admin routes
                .requestMatchers("/api/creator/**").hasRole("CREATOR")  // Creator routes
                .requestMatchers("/api/blogs/**").permitAll()  // Allow all blog routes to be publicly accessible
                .requestMatchers(HttpMethod.POST, "/api/blogs/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.PUT, "/api/blogs/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.PATCH, "/api/blogs/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/**").authenticated()  // Authenticate delete routes
                .anyRequest().authenticated()  // All other routes require authentication
            )
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session management
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before username/password auth

        return http.build();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);


        return provider;
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    	return config.getAuthenticationManager();

    }
}
