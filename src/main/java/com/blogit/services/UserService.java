package com.blogit.services;

import org.springframework.http.ResponseEntity;

import com.blogit.models.User;

public interface UserService {

	User getUserById(Long id);
	
	User updateUser(User user);
	
	void deleteUser(Long id);
	
	User getUserByUsername(String username);
	
	User getUserByEmail(String email);
	
	User getUserByUsernameAndPassword(String username, String password);
	
	User getUserByEmailAndPassword(String email, String password);
	
	User registerUser(User user);
	
	ResponseEntity<User> verify(User user);
	
}
