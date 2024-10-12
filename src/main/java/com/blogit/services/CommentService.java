package com.blogit.services;

import java.util.List;

import com.blogit.models.Comment;

public interface CommentService {

	Comment getCommentById(Long id);

	Comment addComment(Comment comment, String token);

	Comment updateComment(Long id, Comment comment, String token);

	void deleteComment(Long id, String token);

	List<Comment> getCommentsByBlogId(Long blogId);

	List<Comment> getCommentsByUserId(Long userId);
	
}
