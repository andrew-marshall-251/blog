package com.andrew.blog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewPasswordRequest {
	private String oldPassword;
	private String newPassword;
}
