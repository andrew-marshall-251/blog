package com.andrew.blog.services;

import com.andrew.blog.dtos.LoginRequest;
import com.andrew.blog.dtos.LoginResponse;
import com.andrew.blog.dtos.RegisterRequest;
import com.andrew.blog.dtos.RegisterResponse;
import com.andrew.blog.entities.User;
import com.andrew.blog.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
	private UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder encoder;
	private SecretKey signingKey;

	public AuthServiceImpl (
			UserRepository userRepository,
			AuthenticationManager authenticationManager,
			SecretKey signingKey,
			PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.signingKey = signingKey;
		this.encoder = encoder;
	}

	@Override
	public RegisterResponse registerUser(RegisterRequest request) {
		User user = new User();
		user.setBio(request.getBio());
		user.setEmail(request.getEmail());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setUsername(request.getUsername());
		user.setMascot(request.getMascot());
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		roles.add("ROLE_ADMIN");
		user.setRoles(roles);
		userRepository.save(user);

		RegisterResponse response = new RegisterResponse();
		response.setBio(user.getBio());
		response.setUserId(user.getId());
		response.setEmail(user.getEmail());
		response.setUsername(user.getUsername());
		response.setMascot(user.getMascot());
		return response;
	}

	@Override
	public LoginResponse loginUser(LoginRequest request) {
		Authentication authRequest =
				new UsernamePasswordAuthenticationToken(
						request.getUsernameOrEmail(),
						request.getPassword());
		authenticationManager.authenticate(authRequest);

		// User user = userRepository.findByUsername(request.getUsername());
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		roles.add("ROLE_ADMIN");

		Instant now = Instant.now();
		Instant exp = now.plusSeconds(1000);

		String accessToken = Jwts.builder()
				.subject(authRequest.getName())
				.claim("roles", roles)
				.issuedAt(Date.from(now))
				.expiration(Date.from(exp))
				.signWith(signingKey, Jwts.SIG.HS256)
				.compact();

		LoginResponse response = new LoginResponse();
		response.setAccessToken(accessToken);
		response.setTokenType("Bearer");
		response.setExpiresIn(1000);
		response.setUsername(authRequest.getName());
		response.setRoles(roles);
		return response;
	}
}
