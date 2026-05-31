package com.andrew.blog.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.andrew.blog.repositories.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/error", "/public/**", "/api/threads").permitAll()
						.requestMatchers("/api/auth/register").permitAll()
						.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/api/posts").permitAll()
						.requestMatchers("/api/users/**").hasRole("USER")
						.requestMatchers("/api/posts/**").hasRole("USER")
						.requestMatchers("/api/admin").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	SecretKey jwtSigningKey(@Value("${app.jwt.secret}") String secret) {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:4200",
				"http://127.0.0.1:4200",
				"null"
		));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService users(UserRepository userRepository) {
		return username -> {
			com.andrew.blog.entities.User user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
			return User.builder()
					.username(user.getUsername())
					.password(user.getPassword())
					.authorities(user.getRoles()
							.stream()
							.map(SimpleGrantedAuthority::new)
							.collect(Collectors.toList()))
					.build();
		};
	}

//	====TEST USERS====
//	@Bean
//	public UserDetailsService users(PasswordEncoder encoder) {
//		UserDetails user = User.builder()
//				.username("user")
//				.password(encoder.encode("1234"))
//				.roles("USER")
//				.build();
//		UserDetails admin = User.builder()
//				.username("admin")
//				.password(encoder.encode("2345"))
//				.roles("USER", "ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(user, admin);
//	}
}
