package com.andrew.blog.services;

import com.andrew.blog.dtos.*;
import org.springframework.security.core.Authentication;

public interface MeService {
	UserProfileResponse getMyUser(Authentication auth);
	UserPostsResponse getMyPosts(Authentication auth);
	NewPasswordResponse updatePassword(NewPasswordRequest request, Authentication auth);
	EditProfileResponse updateProfile(EditProfileRequest request, Authentication auth);
}
