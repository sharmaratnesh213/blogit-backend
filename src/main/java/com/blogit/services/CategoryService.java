package com.blogit.services;

import java.util.List;

import com.blogit.models.Category;

public interface CategoryService {

	List<Category> getAllCategories();
	
	Category getCategoryById(Long id);
	
	Category createCategory(Category category, String token);
	
	Category updateCategory(Long id, Category category, String token);
	
	void deleteCategory(Long id, String token);
	
	List<Category> getCategoryByName(String name);
	
}
