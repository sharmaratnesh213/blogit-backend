package com.blogit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.Blog;
import com.blogit.services.BlogService;

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
	
	@PutMapping("/{id}/update")
	public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
		Blog updatedBlog = blogService.updateBlog(id, blog);
		if (updatedBlog == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<HttpStatus> deleteBlog(@PathVariable Long id) {
		blogService.deleteBlog(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
