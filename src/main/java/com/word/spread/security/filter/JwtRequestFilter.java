package com.word.spread.security.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.word.spread.service.JwtHelper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final String REFRESH_TOKEN_URI = "/api/auth/refresh";
	protected final JwtHelper jwtHelper;
	protected final UserDetailsService userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String authString = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authString != null && validateAuthString(uri, authString)) {
			DecodedJWT decodedJwt = jwtHelper.decode(authString);
			String subject = decodedJwt.getSubject();
			UserDetails user = userRepo.loadUserByUsername(subject);
			Collection<? extends GrantedAuthority> roles = user.getAuthorities();
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(subject, null, roles);
			WebAuthenticationDetails authenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
			token.setDetails(authenticationDetails);
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		filterChain.doFilter(request, response);
	}
	
	protected boolean validateAuthString(String uri, String authString) {
		if (REFRESH_TOKEN_URI.equals(uri)) {
			return jwtHelper.validateRefreshToken(authString);
		}
		return jwtHelper.validateAccessToken(authString);
	}

}
