package com.blogit.services;

import java.util.List;

import com.blogit.models.Blog;

public interface LikeService {

	void likeBlog(Long blogId, String token);

	void unlikeBlog(Long blogId, String token);
	
	boolean hasUserLikedBlog(Long blogId, Long userId);
	
	long countLikesOnBlog(Long blogId);
	
	List<Blog> getBlogsLikedByUser(Long userId);
	
}
