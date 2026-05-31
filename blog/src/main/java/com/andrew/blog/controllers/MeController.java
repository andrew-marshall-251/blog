package com.andrew.blog.controllers;

import com.andrew.blog.dtos.*;
import com.andrew.blog.services.MeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MeController {
	private MeService meService;

	public MeController(MeService meService) {
		this.meService = meService;
	}
	@GetMapping("/users/me")
	public UserProfileResponse getMyUser(Authentication auth) {
		UserProfileResponse response = meService.getMyUser(auth);
		return response;
	}

	@GetMapping("/posts/me")
	public UserPostsResponse getMyPosts(Authentication auth) {
		UserPostsResponse response = meService.getMyPosts(auth);
		return response;
	}

	@PostMapping("/posts/me")
	public ResponseEntity<NewPostResponse> addNewPost(@Valid @RequestBody NewPostRequest request, Authentication auth) {
		NewPostResponse response = meService.addNewPost(request, auth);
		URI location = URI.create("/api/posts/" + response.getPostId());
		return ResponseEntity.created(location).body(response);
	}

	@PatchMapping("/users/me/password")
	public NewPasswordResponse updatePassword(
			@Valid @RequestBody NewPasswordRequest request,
			Authentication auth) {
		NewPasswordResponse response = meService.updatePassword(request, auth);
		return response;
	}

	@PatchMapping("/users/me")
	public EditProfileResponse updateProfile(
			@Valid @RequestBody EditProfileRequest request,
			Authentication auth) {
		EditProfileResponse response = meService.updateProfile(request, auth);
		return response;
	}
}
