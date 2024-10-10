package com.blogit.services;

import java.util.List;

import com.blogit.models.Blog;

public interface BlogService {

	List<Blog> getAllBlogs();
	
	Blog getBlogById(Long id);
	
	Blog saveBlog(Blog blog);
	
	Blog updateBlog(Blog blog);
	
	void deleteBlog(Long id);
	
	List<Blog> getBlogsByUserId(Long userId);
	
	List<Blog> getBlogsByCategoryId(Long categoryId);
	
	List<Blog> getBlogsByTitle(String title);
	
}
