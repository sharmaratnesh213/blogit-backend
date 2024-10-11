package com.blogit.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogit.exceptions.OperationNotAllowedException;
import com.blogit.exceptions.ResourceNotFoundException;
import com.blogit.models.Category;
import com.blogit.models.User;
import com.blogit.repositories.CategoryRepository;
import com.blogit.repositories.UserRepository;
import com.blogit.services.CategoryService;
import com.blogit.services.JwtService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Category createCategory(Category category, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		if (!user.getRole().equals("ADMIN")) {
            throw new OperationNotAllowedException("Category", "user.role", user.getRole(), "Operation restricted to ADMIN.");
		}
		
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Long id, Category category, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		if (!user.getRole().equals("ADMIN")) {
			throw new OperationNotAllowedException("Category", "user.role", user.getRole(), "Operation restricted to ADMIN.");
		}
		
		Optional<Category> existingCategory = categoryRepository.findById(id);
		
		if (!existingCategory.isPresent()) {
			throw new ResourceNotFoundException("Category", "id", id);
		}
		
		if (category.getName() != null) {
			existingCategory.get().setName(category.getName());
		}
		
		return categoryRepository.save(existingCategory.get());
	}

	@Override
	public void deleteCategory(Long id, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		if (!user.getRole().equals("ADMIN")) {
			throw new OperationNotAllowedException("Category", "user.role", user.getRole(),
					"Operation restricted to ADMIN.");
		}
		
		categoryRepository.deleteById(id);
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByNameContainingIgnoreCase(name);
	}
}
