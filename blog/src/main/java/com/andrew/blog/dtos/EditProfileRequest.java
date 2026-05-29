package com.andrew.blog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EditProfileRequest {
	private Long id;
	private String username;
	private String email;
	private String mascot;
	private String bio;
	private String password;
}
