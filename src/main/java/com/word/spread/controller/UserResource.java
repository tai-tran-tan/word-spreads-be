package com.word.spread.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.word.spread.model.User;
import com.word.spread.repository.UserRepo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE, 
	consumes = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(originPatterns = "*")
@Slf4j
@RequiredArgsConstructor
public class UserResource {
	
	private final PasswordEncoder encoder;
	private final UserRepo userRepo;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public User createUser(@RequestBody UserInfoForm form) {
		log.info("Received register request {}", form);
		
		return userRepo.findById(form.getUsername())
				.orElseGet(() -> {
					User user = new User(form.getUsername());
					user.setPassword(encoder.encode(form.getPassword()));
					userRepo.save(user);
					return user;
				});
	}

	@Data
	public static class UserInfoForm {
		private String username;
		private String password;
	}

}
