package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	
	List<Blog> findByUserId(Long userId);

	List<Blog> findByCategoryId(Long categoryId);

	List<Blog> findByTitle(String title);

}
