package com.blogit.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blogit.exceptions.OperationNotAllowedException;
import com.blogit.exceptions.ResourceNotFoundException;
import com.blogit.models.Blog;
import com.blogit.models.Comment;
import com.blogit.models.User;
import com.blogit.repositories.BlogRepository;
import com.blogit.repositories.CommentRepository;
import com.blogit.repositories.UserRepository;
import com.blogit.services.CommentService;
import com.blogit.services.JwtService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.findById(id).orElse(null);
	}
	
	@Override
	public Comment addComment(Comment comment, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		Optional<Blog> blog = blogRepository.findById(comment.getBlog().getId());
		
		if (!blog.isPresent()) {
			throw new ResourceNotFoundException("Blog", "id", comment.getBlog().getId().toString());
		}
		
		comment.setUser(user);
		comment.setBlog(blog.get());
		comment.setCreationDateTime(LocalDateTime.now());
		comment.setUpdateDateTime(null);
		
		return commentRepository.save(comment);
	}
	
	@Override
	public Comment updateComment(Long id, Comment comment, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}

		Comment existingComment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id.toString()));

		if (!existingComment.getUser().getId().equals(user.getId())) {
			throw new OperationNotAllowedException("Comment", "id", id.toString(), "Operation restricted to authenticated user.");
		}

		existingComment.setContent(comment.getContent());
		existingComment.setUpdateDateTime(LocalDateTime.now());

		return commentRepository.save(existingComment);
	}
	
	@Override
	public void deleteComment(Long id, String token) {
        String email = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id.toString()));
        
        if (!existingComment.getUser().getId().equals(user.getId())) {
			throw new OperationNotAllowedException("Comment", "id", id.toString(),
					"Operation restricted to authenticated user.");
		}
        
        commentRepository.deleteById(id);
	}
	
	@Override
	public List<Comment> getCommentsByBlogId(Long blogId) {
		return commentRepository.findByBlogId(blogId);
	}
	
	@Override
	public List<Comment> getCommentsByUserId(Long userId) {
		return commentRepository.findByUserId(userId);
	}
	
}
