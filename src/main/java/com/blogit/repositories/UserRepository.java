package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

	User findByUsernameAndPassword(String username, String password);

	User findByEmailAndPassword(String email, String password);
	
	List<User> findByUsernameContainingIgnoreCase(String username);

}
