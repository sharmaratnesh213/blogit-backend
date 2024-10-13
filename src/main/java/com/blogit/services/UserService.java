package com.blogit.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.blogit.models.User;

public interface UserService {
	
	List<User> getAllUsers();

	User getUserById(Long id);
	
	User updateUser(Long id, User user, String token);
	
	void deleteUser(Long id, String token);
	
	List<User> getUserByUsername(String username);
	
	User getUserByEmail(String email);
	
	User getUserByUsernameAndPassword(String username, String password);
	
	User getUserByEmailAndPassword(String email, String password);
	
	User registerUser(User user);
	
	ResponseEntity<User> verify(User user);
	
	ResponseEntity<Boolean> verifyToken(String token);
	
	ResponseEntity<String> logout();
	
}
