package com.blogit.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogit.models.User;
import com.blogit.repositories.UserRepository;
import com.blogit.services.JwtService;
import com.blogit.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password).orElse(null);
	}

	@Override
	public User getUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password).orElse(null);
	}

	@Override
	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
	}
	
	@Override
	public ResponseEntity<User> verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
        	User authenticatedUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        	authenticatedUser.setPassword(null);
			String token = jwtService.generateToken(authenticatedUser.getEmail());
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(authenticatedUser);
        } else {
        	return ResponseEntity.status(401).build();
        }
    }
	
	
}
