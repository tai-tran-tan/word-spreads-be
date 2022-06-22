package com.word.spread.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.word.spread.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomizedAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	public CustomizedAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		log.info("Authenticated! Creating tokens...");
		
		User user = (User) authResult.getPrincipal();
		String userName = user.getUsername();
		Algorithm algorithm = Algorithm.HMAC256("my-deep-dark-secret");
		String access_token = JWT.create()
			.withIssuer("word-spreads")
			.withSubject(userName)
			.withExpiresAt(createTokenExpiryDate(30 * 1000)) //30 secs
			.withClaim("roles", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(algorithm);

		String refresh_token = JWT.create()
				.withIssuer("word-spreads")
				.withSubject(userName)
				.withExpiresAt(createTokenExpiryDate(5 * 60 * 1000)) //5 mins
				.sign(algorithm);

		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}

	private Date createTokenExpiryDate(long period) {
		return new Date(System.currentTimeMillis() + period);
	}
	
}
