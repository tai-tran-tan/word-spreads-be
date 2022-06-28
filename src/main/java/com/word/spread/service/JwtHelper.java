package com.word.spread.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.word.spread.model.Role;
import com.word.spread.model.User;
import com.word.spread.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHelper {
	private static final Algorithm ALGORITHM = Algorithm.HMAC256("my-deep-dark-secret");

	private final UserRepo userRepo;
	
	public Map<String, String> createTokens(String userName, Collection<? extends GrantedAuthority> authorities) {
		String access_token = JWT.create()
			.withIssuer("word-spreads")
			.withSubject(userName)
			.withExpiresAt(createTokenExpiryDate(1 * 60 * 1000)) //10 mins
			.withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(ALGORITHM);

		String refresh_token = JWT.create()
				.withIssuer("word-spreads")
				.withSubject(userName)
				.withExpiresAt(createTokenExpiryDate(30 * 60 * 1000)) //30 mins
				.sign(ALGORITHM);

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		return tokens;
	}

	private static Date createTokenExpiryDate(long period) {
		return new Date(System.currentTimeMillis() + period);
	}

	public DecodedJWT decode(String jwt) {
		return JWT.require(ALGORITHM).build().verify(jwt);
	}

	public boolean validate(String jwt) {
		DecodedJWT decodedJwt = decode(jwt);
		User user = userRepo.findByUsername(decodedJwt.getSubject());
		String[] roles = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList()).toArray(new String[] {});
		JWT.require(ALGORITHM).withSubject(user.getUsername()).withArrayClaim("roles", roles).build().verify(jwt);
		return true;
	}
	
}
