package com.word.spread.security.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.word.spread.model.Role;
import com.word.spread.service.JwtHelper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";
	
	private final JwtHelper jwtHelper;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authString = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authString != null && authString.startsWith(BEARER_PREFIX)) {
			String jwt = authString.replace(BEARER_PREFIX, "");
			DecodedJWT decodedJwt = jwtHelper.decode(jwt);
			String subject = decodedJwt.getSubject();
//			UserDetails userDetails = userService.loadUserByUsername(subject);
			if (jwtHelper.validate(jwt)) {
				List<Role> roles = decodedJwt.getClaim("roles").asList(Role.class);
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(subject, null, roles);
				WebAuthenticationDetails authenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
				token.setDetails(authenticationDetails);
				SecurityContextHolder.getContext().setAuthentication(token);
			}
		}
		filterChain.doFilter(request, response);
	}

}
