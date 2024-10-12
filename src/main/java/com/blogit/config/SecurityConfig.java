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
                .requestMatchers("api/auth/logout").authenticated()  // Logout route
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Admin routes
                .requestMatchers("/api/creator/**").hasRole("CREATOR")  // Creator routes
                .requestMatchers("/swagger-ui/**").permitAll()  // Allow Swagger UI
                .requestMatchers("/v3/api-docs/**").permitAll()  //Allow Swagger UI
                .requestMatchers("/api/blogs/**").permitAll()  // Allow all blog routes to be publicly accessible
                .requestMatchers(HttpMethod.POST, "/api/blogs/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.PUT, "/api/blogs/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.PATCH, "/api/blogs/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/**").authenticated()  // Authenticate delete routes
                .requestMatchers("/api/categories/**").permitAll()  // Allow all category routes to be publicly accessible
                .requestMatchers(HttpMethod.POST, "/api/categories/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.PUT, "/api/categories/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.PATCH, "/api/categories/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").authenticated()  // Authenticate delete routes
                .requestMatchers("/api/users/**").permitAll()  // Allow all user routes to be publicly accessible
                .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()  // Authenticate delete routes
                .requestMatchers("/api/comments/**").permitAll()  // Allow all comment routes to be publicly accessible
                .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.PATCH, "/api/comments/**").authenticated()  // Authenticate update routes
                .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()  // Authenticate delete routes
                .requestMatchers("/api/likes/**").permitAll()  // Allow all like routes to be publicly accessible")
                .requestMatchers(HttpMethod.POST, "/api/likes/**").authenticated()  // Authenticate create routes
                .requestMatchers(HttpMethod.DELETE, "/api/likes/**").authenticated()  // Authenticate delete routes
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
