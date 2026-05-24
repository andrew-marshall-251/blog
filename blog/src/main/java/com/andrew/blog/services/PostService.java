package com.andrew.blog.services;

import com.andrew.blog.dtos.NewPostRequest;
import com.andrew.blog.dtos.NewPostResponse;

public interface PostService {
	NewPostResponse addNewPost(NewPostRequest request);
}
