package com.blogit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.Blog;
import com.blogit.services.BlogService;

@CrossOrigin(origins = "http://localhost:4200",
allowedHeaders = {"Authorization", "Content-Type"},
exposedHeaders = {"Authorization"},
allowCredentials = "true",
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} , maxAge = 3600)
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

	@Autowired
	private BlogService blogService;
	
	@PostMapping("/create")
	public ResponseEntity<Blog> createBlog(@RequestBody Blog blog, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(blogService.createBlog(blog, jwtToken), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
		Blog blog = blogService.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(blog, HttpStatus.OK);
    }
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Blog>> getBlogsByUserId(@PathVariable Long userId) {
		List<Blog> blogs = blogService.getBlogsByUserId(userId);
		return new ResponseEntity<>(blogs, HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<Blog>> getBlogsByCategoryId(@PathVariable Long categoryId) {
		List<Blog> blogs = blogService.getBlogsByCategoryId(categoryId);
		return new ResponseEntity<>(blogs, HttpStatus.OK);
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<List<Blog>> getBlogsByTitle(@PathVariable String title) {
        List<Blog> blogs = blogService.getBlogsByTitle(title);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }
	
	@GetMapping("/categoryName/{categoryName}")
	public ResponseEntity<List<Blog>> getBlogsByCategoryName(@PathVariable String categoryName) {
        List<Blog> blogs = blogService.getBlogsByCategoryName(categoryName);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }
	
	@PutMapping("/{id}/update")
	public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		Blog updatedBlog = blogService.updateBlog(id, blog, jwtToken);
		if (updatedBlog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<String> deleteBlog(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		blogService.deleteBlog(id, jwtToken);
		return new ResponseEntity<>("Blog deleted successfully.", HttpStatus.OK);
	}
	
}
