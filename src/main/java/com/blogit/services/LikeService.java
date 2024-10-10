package com.blogit.services;

public interface LikeService {

	void likeBlog(Long blogId, Long userId);

	void unlikeBlog(Long blogId, Long userId);
}
