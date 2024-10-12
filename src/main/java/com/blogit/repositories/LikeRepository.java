package com.blogit.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

	
	List<Like> findByBlogId(Long blogId);
	
	long countByBlogId(Long blogId);
	
	List<Like> findByUserId(Long userId);
	
	long countByUserId(Long userId);
	
	Optional<Like> findByBlogIdAndUserId(Long blogId, Long userId);
	
	boolean existsByBlogIdAndUserId(Long blogId, Long userId);
	
}
