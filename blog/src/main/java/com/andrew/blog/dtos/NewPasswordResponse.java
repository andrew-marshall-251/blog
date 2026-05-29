package com.andrew.blog.dtos;

import com.andrew.blog.entities.Mascot;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewPasswordResponse {
	private Long id;
	private String username;
	private String email;
	private Mascot mascot;
	private String bio;
}
