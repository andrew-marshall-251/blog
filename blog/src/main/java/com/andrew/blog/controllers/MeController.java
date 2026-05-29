package com.andrew.blog.controllers;

import com.andrew.blog.dtos.*;
import com.andrew.blog.services.MeService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
