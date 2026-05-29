package com.andrew.blog.dtos;

import com.andrew.blog.entities.Mascot;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
	@NotBlank(message="Username is required")
	@Size(min=5, max=30, message="Username must be 5-30 chars")
	@Pattern(regexp="^[a-zA-Z0-9_.-]+$", message="Username has invalid characters")
	private String username;

	@NotBlank(message="Email is required")
	@Email(message="Email format is invalid")
	private String email;

	@NotBlank
	@Size(min=8, max=72, message="Password must be 8-72 characters")
	private String password;

	@NotNull(message="Mascot is required")
	private Mascot mascot;

	@NotBlank(message="Bio is required")
	@Size(max=500, message="Bio must be less than 500 characters")
	private String bio;
}
