package com.andrew.blog.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserPostsResponse {
	private List<UserPost> userPosts;
}
