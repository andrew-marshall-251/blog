package com.andrew.blog.services;

import com.andrew.blog.dtos.*;
import com.andrew.blog.entities.*;
import com.andrew.blog.entities.Thread;
import com.andrew.blog.repositories.PostRepository;
import com.andrew.blog.repositories.ThreadRepository;
import com.andrew.blog.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MeServiceImpl implements MeService {
	private UserRepository userRepository;
	private PostRepository postRepository;
	private ThreadRepository threadRepository;
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;

	public MeServiceImpl(
			UserRepository userRepository,
			PostRepository postRepository,
			ThreadRepository threadRepository,
			PasswordEncoder encoder,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.threadRepository = threadRepository;
		this.authenticationManager = authenticationManager;
		this.encoder = encoder;
	}

	@Override
	public UserProfileResponse getMyUser(Authentication auth) {
		String username = auth.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		UserProfileResponse userProfileResponse = new UserProfileResponse();
		userProfileResponse.setId(user.getId());
		userProfileResponse.setUsername(user.getUsername());
		userProfileResponse.setEmail(user.getEmail());
		userProfileResponse.setMascot(user.getMascot());
		userProfileResponse.setBio(user.getBio());
		return userProfileResponse;
	}

	@Override
	public UserPostsResponse getMyPosts(Authentication auth) {
		// get posts as List<Post>
		String username = auth.getName();
		List<Post> myPosts = postRepository.findByAuthorUsername(username);
		// convert to Dto
		List<UserPost> userPostsDto = new ArrayList<UserPost>();
		for (Post myPost: myPosts) {
			UserPost userPostDto = new UserPost();
			userPostDto.setPostTitle(myPost.getPostTitle());
			userPostDto.setPostContent(myPost.getContent());
			String postDate = myPost.getLastUpdatedAt()
					.format(DateTimeFormatter.ofPattern("M/d/yyyy H:mm a"));
			userPostDto.setPostDate(postDate);
			userPostsDto.add(userPostDto);
		}
		// put dto in response wrapper
		UserPostsResponse response = new UserPostsResponse();
		response.setUserPosts(userPostsDto);
		return response;
	}

	public String makeSlug(String postTitle) {
		int len = postTitle.length();
		return postTitle.substring(0, Math.min(5, len));
	}
	public NewPostResponse addNewPost(
			NewPostRequest request,
			Authentication auth) {
		// User
		String username = auth.getName();
		User author = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));
		// Thread
		Long threadId = request.getThreadId();
		Thread thread = threadRepository.findById(threadId)
				.orElseThrow(() -> new RuntimeException("Thread with id: " + threadId + " not found!"));
		// create post
		Post newPost = new Post();
		newPost.setAuthor(author);
		newPost.setThread(thread);
		newPost.setPostTitle(request.getPostTitle());
		newPost.setPostSubTitle("Sub");
		newPost.setStatus(Status.PUBLISHED);
		newPost.setSlug(makeSlug(request.getPostTitle()));
		newPost.setContent(request.getPostContent());
		postRepository.save(newPost);
		// create response
		NewPostResponse response = new NewPostResponse();
		response.setPostId(newPost.getId());
		return response;
	}


	@Override
	public NewPasswordResponse updatePassword(
			NewPasswordRequest request,
			Authentication auth) {
		// Get user from JWT & Check old password
		String username = auth.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));
		Authentication authRequest =
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						request.getOldPassword());
		authenticationManager.authenticate(authRequest);
		// encrypt the new password and update user
		user.setPassword(encoder.encode(request.getNewPassword()));
		userRepository.save(user);
		// create and return DTO
		NewPasswordResponse response = new NewPasswordResponse();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setMascot(user.getMascot());
		response.setBio(user.getBio());
		return response;
	}

	@Override
	public EditProfileResponse updateProfile(
			EditProfileRequest request,
			Authentication auth) {
		// Get user from JWT & Validate password
		String username = auth.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));
		Authentication authRequest =
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						request.getPassword());
		authenticationManager.authenticate(authRequest);
		// check for duplicates in db
		Optional<User> usernameComp = userRepository.findByUsername(
				request.getUsername());
		Optional<User> emailComp = userRepository.findByEmail(
				request.getEmail());
		if (usernameComp.isPresent() &&
			!user.getId().equals(usernameComp.get().getId())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"username taken");
		}
		if (emailComp.isPresent() &&
				!user.getId().equals(emailComp.get().getId())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"email taken");
		}
		// update user
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setMascot(Mascot.valueOf(request.getMascot()));
		user.setBio(request.getBio());
		userRepository.save(user);
		// create and return DTO
		EditProfileResponse response = new EditProfileResponse();
		response.setUserId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setMascot(user.getMascot());
		response.setBio(user.getBio());
		return response;
	}
}
