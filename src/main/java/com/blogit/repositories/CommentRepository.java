package com.blogit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogit.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByBlogId(Long blogId);
	
	List<Comment> findByUserId(Long userId);
	
}
