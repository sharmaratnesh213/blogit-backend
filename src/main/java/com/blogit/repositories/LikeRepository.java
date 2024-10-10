package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

	
	List<Like> findByBlogId(Long blogId);
	
	List<Like> findByUserId(Long userId);
	
	Like findByBlogIdAndUserId(Long blogId, Long userId);
	
	boolean existsByBlogIdAndUserId(Long blogId, Long userId);
	
}
