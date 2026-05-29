package com.andrew.blog.services;

import com.andrew.blog.dtos.NewPostRequest;
import com.andrew.blog.dtos.NewPostResponse;
import org.springframework.security.core.Authentication;

public interface PostService {
	NewPostResponse addNewPost(NewPostRequest request, Authentication auth);
}
