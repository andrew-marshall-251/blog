package com.andrew.blog.controllers;

import com.andrew.blog.dtos.LoginRequest;
import com.andrew.blog.dtos.LoginResponse;
import com.andrew.blog.dtos.RegisterRequest;
import com.andrew.blog.dtos.RegisterResponse;
import com.andrew.blog.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {
		RegisterResponse response = authService.registerUser(request);
		return response;
	}

	@PostMapping("/login")
	public LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = authService.loginUser(request);
		return response;
	}
}
