package com.blogit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogit.models.Blog;
import com.blogit.services.LikeService;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

	@Autowired
	private LikeService likeService;
	
	@PostMapping("/like/blog/{blogId}")
	public ResponseEntity<String> likeBlog(@PathVariable Long blogId, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		likeService.likeBlog(blogId, jwtToken);
		return new ResponseEntity<>("Blog liked successfully.", HttpStatus.OK);
	}
	
	@DeleteMapping("/unlike/blog/{blogId}")
	public ResponseEntity<String> unlikeBlog(@PathVariable Long blogId, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		likeService.unlikeBlog(blogId, jwtToken);
		return new ResponseEntity<>("Blog unliked successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/blog/{blogId}/user/{userId}")
	public ResponseEntity<Boolean> hasUserLikedBlog(@PathVariable Long blogId, @PathVariable Long userId) {
		return new ResponseEntity<>(likeService.hasUserLikedBlog(blogId, userId), HttpStatus.OK);
	}
	
	@GetMapping("/blog/{blogId}/count")
	public ResponseEntity<Long> getLikesCount(@PathVariable Long blogId) {
		return new ResponseEntity<>(likeService.countLikesOnBlog(blogId), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Blog>> getBlogsLikedByUser(@PathVariable Long userId) {
		return new ResponseEntity<>(likeService.getBlogsLikedByUser(userId), HttpStatus.OK);
	}
	
}
