package com.blogit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.User;
import com.blogit.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<List<User>> getUserByUsername(@PathVariable String username) {
		List<User> users = userService.getUserByUsername(username);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PutMapping("{id}/update")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(userService.updateUser(id, user, jwtToken), HttpStatus.OK);
	}
	
	@DeleteMapping("{id}/delete")
	public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		userService.deleteUser(id, jwtToken);
		return new ResponseEntity<>("Account deleted successfully.", HttpStatus.OK);
	}
	
}
