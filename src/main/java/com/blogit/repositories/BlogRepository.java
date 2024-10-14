package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.blogit.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
	
	List<Blog> findByUserId(Long userId);

	List<Blog> findByCategoryId(Long categoryId);

	List<Blog> findByTitle(String title);
	
	List<Blog> findByTitleContainingIgnoreCase(String title);
	
	List<Blog> findByCategory_NameContainingIgnoreCase(String categoryName);

}
