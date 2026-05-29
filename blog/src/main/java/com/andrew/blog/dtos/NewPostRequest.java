package com.andrew.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewPostRequest {
	@NotNull
	private Long threadId;
	@NotBlank
	@Size(min = 5, max = 150)
	private String postTitle;
	@NotBlank
	@Size(max = 2000)
	private String postContent;
}
