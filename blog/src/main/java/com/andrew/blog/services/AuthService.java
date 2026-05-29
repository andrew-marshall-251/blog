package com.andrew.blog.services;

import com.andrew.blog.dtos.LoginRequest;
import com.andrew.blog.dtos.LoginResponse;
import com.andrew.blog.dtos.RegisterRequest;
import com.andrew.blog.dtos.RegisterResponse;

public interface AuthService {
	RegisterResponse registerUser(RegisterRequest request);
	LoginResponse loginUser(LoginRequest request);
}
