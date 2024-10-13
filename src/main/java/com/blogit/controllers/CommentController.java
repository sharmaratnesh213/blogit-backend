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

import com.blogit.models.Comment;
import com.blogit.services.CommentService;

@CrossOrigin(origins = "http://localhost:4200",
allowedHeaders = {"Authorization", "Content-Type"},
exposedHeaders = {"Authorization"},
allowCredentials = "true",
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} , maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/add")
	public ResponseEntity<Comment> addComment(@RequestBody Comment comment,
			@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(commentService.addComment(comment, jwtToken), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
		Comment comment = commentService.getCommentById(id);
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}
	
	@GetMapping("/blog/{blogId}")
	public ResponseEntity<List<Comment>> getCommentsByBlogId(@PathVariable Long blogId) {
		List<Comment> comments = commentService.getCommentsByBlogId(blogId);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
		List<Comment> comments = commentService.getCommentsByUserId(userId);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	
	@PutMapping("/{id}/update")
	public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment,
			@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		return new ResponseEntity<>(commentService.updateComment(id, comment, jwtToken), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
		commentService.deleteComment(id, jwtToken);
		return new ResponseEntity<>("Comment deleted successfully.", HttpStatus.OK);
	}
	
}
