package com.blogit.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogit.exceptions.OperationNotAllowedException;
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
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User updateUser(Long id, User user, String token) {
		String email = jwtService.extractUserName(token);
		User authenticatedUser = userRepository.findByEmail(email);
		
		if (authenticatedUser.getId() != id) {
			throw new OperationNotAllowedException("User", "id", user.getId().toString(), "Operation restricted to authenticated user.");
		}
		
		if (user.getPassword() != null) {
			authenticatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		if (user.getImageUrl() != null) {
			authenticatedUser.setImageUrl(user.getImageUrl());
		}
		
		if (user.getUsername() != null) {
			authenticatedUser.setUsername(user.getUsername());
		}
		
		if (user.getDob() != null) {
			authenticatedUser.setDob(user.getDob());
		}
		
		return userRepository.save(authenticatedUser);
	}

	@Override
	public void deleteUser(Long id, String token) {
		String email = jwtService.extractUserName(token);
		User authenticatedUser = userRepository.findByEmail(email);
		
		if (authenticatedUser.getId() != id) {
			throw new OperationNotAllowedException("User", "id", String.valueOf(id),
					"Operation restricted to authenticated user.");
		}
		
		userRepository.deleteById(id);
	}

	@Override
	public List<User> getUserByUsername(String username) {
		return userRepository.findByUsernameContainingIgnoreCase(username);
	}

	@Override
	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	@Override
	public User getUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
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
        	User authenticatedUser = userRepository.findByEmail(user.getEmail());
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
	
	@Override
	public ResponseEntity<Boolean> verifyToken(String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			return ResponseEntity.ok(false);
		}
		
		if (jwtService.isTokenExpired(token)) {
			return ResponseEntity.ok(false);
		} else {
			return ResponseEntity.ok(true);
		}
	}
	
	@Override
	public ResponseEntity<String> logout() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer ");
		return ResponseEntity.ok()
				.headers(headers)
				.body("Logged out successfully.");
	}
	
	
}
