package com.andrew.blog.controllers;

import com.andrew.blog.dtos.NewPostResponse;
import com.andrew.blog.dtos.NewPostRequest;
import com.andrew.blog.services.PostServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
public class PostController {
	private PostServiceImpl postService;

	public PostController(PostServiceImpl postService) {
		this.postService = postService;
	}

	@PostMapping("/")
	public ResponseEntity<NewPostResponse> addNewPost(@Valid @RequestBody NewPostRequest request) {
		NewPostResponse response = postService.addNewPost(request);
		URI location = URI.create("/api/posts/" + response.getPostId());
		return ResponseEntity.created(location).body(response);
	}
}
