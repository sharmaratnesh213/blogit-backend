package com.blogit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.Category;
import com.blogit.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> categories = categoryService.getAllCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
		Category category = categoryService.getCategoryById(id);
		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category, HttpStatus.OK);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
		Category category = categoryService.getCategoryByName(name);
		if (category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Category> createCategory(@RequestBody Category category, 
			@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(categoryService.createCategory(category, jwtToken), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}/update")
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category,
			@RequestHeader("Authorization") String token) {
		
		String jwtToken = token.substring(7);
		Category updatedCategory = categoryService.updateCategory(id, category, jwtToken);
		if (updatedCategory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		categoryService.deleteCategory(id, jwtToken);
		return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
	}
	
}
