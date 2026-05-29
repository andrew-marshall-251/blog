package com.andrew.blog.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class LoginResponse {
	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private String username;
	private List<String> roles;
}
