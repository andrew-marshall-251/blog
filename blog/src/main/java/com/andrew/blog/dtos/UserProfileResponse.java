package com.andrew.blog.dtos;

import com.andrew.blog.entities.Mascot;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserProfileResponse {
	private Long id;
	private String username;
	private String email;
	private Mascot mascot;
	private String bio;
}
