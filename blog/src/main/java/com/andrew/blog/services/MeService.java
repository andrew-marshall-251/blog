package com.andrew.blog.services;

import com.andrew.blog.dtos.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

public interface MeService {
	UserProfileResponse getMyUser(Authentication auth);
	UserPostsResponse getMyPosts(Authentication auth);
	NewPostResponse addNewPost(NewPostRequest request, Authentication auth);
	NewPasswordResponse updatePassword(NewPasswordRequest request, Authentication auth);
	EditProfileResponse updateProfile(EditProfileRequest request, Authentication auth);
}
