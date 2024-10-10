package com.blogit.services;

import java.util.List;

import com.blogit.models.Comment;

public interface CommentService {

	Comment getCommentById(Long id);

	Comment saveComment(Comment comment, Long blogId, Long userId);

	Comment updateComment(Comment comment, Long blogId, Long userId);

	void deleteComment(Long id);

	List<Comment> getCommentsByBlogId(Long blogId);

	List<Comment> getCommentsByUserId(Long userId);
	
}
