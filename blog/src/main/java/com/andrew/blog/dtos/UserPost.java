package com.andrew.blog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPost {
	private String postTitle;
	private String postContent;
	private String postDate;
}
