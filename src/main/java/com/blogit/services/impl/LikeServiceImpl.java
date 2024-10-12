package com.blogit.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogit.exceptions.OperationNotAllowedException;
import com.blogit.exceptions.ResourceNotFoundException;
import com.blogit.models.Blog;
import com.blogit.models.Like;
import com.blogit.models.User;
import com.blogit.repositories.BlogRepository;
import com.blogit.repositories.LikeRepository;
import com.blogit.repositories.UserRepository;
import com.blogit.services.JwtService;
import com.blogit.services.LikeService;

@Service
public class LikeServiceImpl implements LikeService {
	
	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	
	@Override
	@Transactional
	public void likeBlog(Long blogId, String token) {
		String email = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(email);
        
        if(user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        Blog blog = blogRepository.findById(blogId)
        		.orElseThrow(() -> new ResourceNotFoundException("Blog", "id", blogId.toString()));
        
        if (likeRepository.existsByBlogIdAndUserId(blogId, user.getId())) {
        	throw new OperationNotAllowedException("Like", "blogId", blogId.toString(), "User already liked this blog.");
        }
        
        Like like = new Like();
        like.setBlog(blog);
        like.setUser(user);
        likeRepository.save(like);
	}
	
	@Override
	@Transactional
	public void unlikeBlog(Long blogId, String token) {
		String email = jwtService.extractUserName(token);
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}

		if (!blogRepository.existsById(blogId)) {
			throw new ResourceNotFoundException("Blog", "id", blogId.toString());
		}

		Like like = likeRepository.findByBlogIdAndUserId(blogId, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Like", "blogId", blogId.toString()));

		likeRepository.delete(like);
	}
	
	@Override
	public boolean hasUserLikedBlog(Long blogId, Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

		if (!blogRepository.existsById(blogId)) {
			throw new ResourceNotFoundException("Blog", "id", blogId.toString());
		}

		return likeRepository.existsByBlogIdAndUserId(blogId, user.getId());
	}
	
	@Override
	public long countLikesOnBlog(Long blogId) {
		if (!blogRepository.existsById(blogId)) {
			throw new ResourceNotFoundException("Blog", "id", blogId.toString());
		}

		return likeRepository.countByBlogId(blogId);
	}
	
	@Override
	public List<Blog> getBlogsLikedByUser(Long userId) {
        User user = userRepository.findById(userId)
        		.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        
        List<Like> likes = likeRepository.findByUserId(user.getId());
        List<Blog> blogs = likes.stream()
        		.map(like -> like.getBlog())
        		.collect(Collectors.toList());
        
        return blogs;
	}
	
}
