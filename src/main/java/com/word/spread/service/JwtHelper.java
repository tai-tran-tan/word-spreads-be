package com.word.spread.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHelper {
	private static final Algorithm ALGORITHM = Algorithm.HMAC256("my-deep-dark-secret");
	private static final String BEARER_PREFIX = "Bearer ";

	private final UserService userService;
	
	public Map<String, String> createTokens(String userName, Collection<? extends GrantedAuthority> authorities) {
		List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

		String accessToken = createToken(userName, roles, 30 * 1000);
		String refreshToken = createToken(userName, Arrays.asList("USER"), 30 * 60 * 1000);

		return createTokens(accessToken, refreshToken);
	}

	private String createToken(String userName, List<String> list, long age) {
		return JWT.create()
				.withIssuer("word-spreads")
				.withSubject(userName)
				.withIssuedAt(new Date())
				.withClaim("roles", list)
				.withExpiresAt(createTokenExpiryDate(age))
				.sign(ALGORITHM);
	}
	
	public Map<String, String> createTokens(String authString) {
		DecodedJWT jwt = decode(authString);
		if (validateRefreshToken(jwt) ) {
			String subject = jwt.getSubject();
			UserDetails details = userService.loadUserByUsername(subject);
			return createTokens(subject, details.getAuthorities());
		}
		throw new BadCredentialsException("Provided token is invalid!");
	}
	
	public Map<String, String> createTokens(String accessToken, String refreshToken) {
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		return tokens;
	}
	
	private static Date createTokenExpiryDate(long period) {
		return new Date(System.currentTimeMillis() + period);
	}

	public DecodedJWT decode(String authString) {
		if (authString != null && authString.startsWith(BEARER_PREFIX)) {
			String jwt = authString.replace(BEARER_PREFIX, "");
			return JWT.require(ALGORITHM).withArrayClaim("roles", "USER").build().verify(jwt);
		}
		throw new BadCredentialsException("Provided token is invalid!");
	}

	public boolean validateRefreshToken(String authString) {
		DecodedJWT decodedJwt = decode(authString);
		return validateRefreshToken(decodedJwt);
	}

	private boolean validateRefreshToken(DecodedJWT decodedJwt) {
		return decodedJwt != null;
	}
	
	public boolean validateAccessToken(String authString) {
		DecodedJWT decodedJwt = decode(authString);
		return validateAccessToken(decodedJwt);
	}

	public boolean validateAccessToken(DecodedJWT decodedJwt) {
		if (decodedJwt != null) {
			UserDetails user = userService.loadUserByUsername(decodedJwt.getSubject());
			String[] roles = user.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList())
					.toArray(new String[] {});
			JWT.require(ALGORITHM).withSubject(user.getUsername()).withArrayClaim("roles", roles).build()
					.verify(decodedJwt);
			return true;

		}
		return false;
	}

	public Map<String, String> refreshToken(String authString) {
		return createTokens(authString);
	}
	
}
