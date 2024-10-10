package com.blogit.services;

import java.util.List;
import java.util.Locale.Category;

public interface CategoryService {

	List<Category> getAllCategories();
	
	Category getCategoryById(Long id);
	
	Category saveCategory(Category category);
	
	Category updateCategory(Category category);
	
	void deleteCategory(Long id);
	
	Category getCategoryByName(String name);
	
}
