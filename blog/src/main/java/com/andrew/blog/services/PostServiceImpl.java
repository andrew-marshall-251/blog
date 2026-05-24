package com.andrew.blog.services;

import com.andrew.blog.dtos.NewPostResponse;
import com.andrew.blog.dtos.NewPostRequest;
import com.andrew.blog.entities.Post;
import com.andrew.blog.entities.Status;
import com.andrew.blog.entities.User;
import com.andrew.blog.entities.Thread;
import com.andrew.blog.repositories.PostRepository;
import com.andrew.blog.repositories.ThreadRepository;
import com.andrew.blog.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl {
	private final ThreadRepository threadRepository;
	private PostRepository postRepository;
	private UserRepository userRepository;

	public PostServiceImpl(
			PostRepository postRepository,
			UserRepository userRepository, ThreadRepository threadRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.threadRepository = threadRepository;
	}

	public String makeSlug(String postTitle) {
		int len = postTitle.length();
		return postTitle.substring(0, Math.min(5, len));
	}
	public NewPostResponse addNewPost(NewPostRequest request) {
		Post newPost = new Post();
		NewPostResponse response = new NewPostResponse();

		Long authorId = request.getAuthorId();
		Long threadId = request.getThreadId();
		User author = userRepository.findById(authorId)
				.orElseThrow(() -> new RuntimeException("User with id: " + authorId + " not found!"));
		Thread thread = threadRepository.findById(threadId)
				.orElseThrow(() -> new RuntimeException("Thread with id: " + authorId + " not found!"));

		newPost.setAuthor(author);
		newPost.setThread(thread);
		newPost.setPostTitle(request.getPostTitle());
		newPost.setPostSubTitle("Sub");
		newPost.setStatus(Status.PUBLISHED);
		newPost.setSlug(makeSlug(request.getPostTitle()));
		newPost.setContent(request.getPostContent());

		postRepository.save(newPost);

		response.setPostId(newPost.getId());
		return response;
	}
}
