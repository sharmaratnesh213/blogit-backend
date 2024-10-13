package com.blogit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.User;
import com.blogit.services.UserService;

@CrossOrigin(origins = "http://localhost:4200",
allowedHeaders = {"Authorization", "Content-Type"},
exposedHeaders = {"Authorization"},
allowCredentials = "true",
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} , maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		return userService.verify(user);
	}
	
	@GetMapping("/verify-token")
	public ResponseEntity<Boolean> verifyToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return userService.verifyToken(jwtToken);
	}
	
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return userService.registerUser(user);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout() {
		return userService.logout();
	}
	
}
