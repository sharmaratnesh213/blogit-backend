package com.blogit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);
	
	Category findByNameContainingIgnoreCase(String name);
	
}
