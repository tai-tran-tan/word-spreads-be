package com.word.spread.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.word.spread.service.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomizedAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final JwtHelper jwtHelper;
	
	public CustomizedAuthenticationFilter(AuthenticationManager authenticationManager, JwtHelper helper) {
		super(authenticationManager);
		this.jwtHelper = helper;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String userName = authResult.getName();
		log.info("User {} authenticated! Creating tokens...", userName);
		
		Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
		Map<String, String> tokens = jwtHelper.createTokens(userName, authorities);

		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
}
