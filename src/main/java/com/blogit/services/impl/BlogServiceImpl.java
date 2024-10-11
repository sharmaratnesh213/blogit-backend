package com.blogit.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogit.exceptions.OperationNotAllowedException;
import com.blogit.exceptions.ResourceNotFoundException;
import com.blogit.models.Blog;
import com.blogit.models.Category;
import com.blogit.models.User;
import com.blogit.repositories.BlogRepository;
import com.blogit.repositories.CategoryRepository;
import com.blogit.repositories.UserRepository;
import com.blogit.services.BlogService;
import com.blogit.services.JwtService;

@Service
public class BlogServiceImpl implements BlogService {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Blog> getAllBlogs() {
		return blogRepository.findAll();
	}
	
	@Override
	public Blog getBlogById(Long id) {
		return blogRepository.findById(id).orElse(null);
	}
	
	@Override
	public Blog createBlog(Blog blog, String token) {
		return saveBlog(blog, token);
	}
	
	@Override
	public Blog saveBlog(Blog blog, String token) {
		String email = jwtService.extractUserName(token);	
		User user = userRepository.findByEmail(email);
		
		Category category = categoryRepository.findById(blog.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
		
		if (user != null) {
            blog.setUser(user);
            blog.setCategory(category);
            blog.setCreationDateTime(LocalDateTime.now());
            blog.setUpdateDateTime(null);
            return blogRepository.save(blog);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
	}
	
	@Override
	public Blog updateBlog(Long id, Blog blog, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		if (blog.getUser() == null || !blog.getUser().getEmail().equals(email)) {
			throw new OperationNotAllowedException("Blog", "user.email", email, "User not allowed to update the blog.");
		}
		
		Optional<Blog> existingBlog = blogRepository.findById(id);
		if (existingBlog.isPresent()) {
			Blog updatedBlog = existingBlog.get();
			if (blog.getTitle() != null) {
				updatedBlog.setTitle(blog.getTitle());
			}
			if (blog.getContent() != null) {
				updatedBlog.setContent(blog.getContent());
			}
			if (blog.getCategory() != null) {
				updatedBlog.setCategory(blog.getCategory());
			}
			return blogRepository.save(updatedBlog);
		} else {
			throw new ResourceNotFoundException("Blog", "id", id);
		}
	}
	
	@Override
	public void deleteBlog(Long id, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		Optional<Blog> blog = blogRepository.findById(id);
		if (!blog.isPresent()) {
			throw new ResourceNotFoundException("Blog", "id", id);
		}
		if (blog.get().getUser() == null || !blog.get().getUser().getEmail().equals(email)) {
			throw new OperationNotAllowedException("Blog", "user.email", email, "User not allowed to delete the blog.");
		}
		blogRepository.deleteById(id);
	}
	
	@Override
	public List<Blog> getBlogsByUserId(Long userId) {
		return blogRepository.findByUserId(userId);
	}
	
	@Override
	public List<Blog> getBlogsByCategoryId(Long categoryId) {
		return blogRepository.findByCategoryId(categoryId);
	}
	
	@Override
	public List<Blog> getBlogsByTitle(String title) {
		return blogRepository.findByTitleContainingIgnoreCase(title);
	}
	
	@Override
	public List<Blog> getBlogsByCategoryName(String categoryName) {
		return blogRepository.findByCategory_NameContainingIgnoreCase(categoryName);
	}
	
}
