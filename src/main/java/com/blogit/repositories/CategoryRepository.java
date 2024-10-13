package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);
	
	List<Category> findByNameContainingIgnoreCase(String name);
	
}
