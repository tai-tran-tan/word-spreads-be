package com.word.spread.security;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.word.spread.model.User;
import com.word.spread.repository.UserRepo;

import lombok.RequiredArgsConstructor;

//@Service
@RequiredArgsConstructor
public class CustomizedAuthenticationManager {
//implements AuthenticationManager {

	private final UserRepo userRepo;
	private final PasswordEncoder encoder;
	
//	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String pwd = encoder.encode(String.valueOf(authentication.getCredentials()));
		Optional<User> findingUser = userRepo.findById(name);
		return findingUser
				.filter(user -> user.getPassword().equals(pwd))
				.map(user -> authentication)
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
	}

}
