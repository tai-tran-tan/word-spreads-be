package com.word.spread.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.word.spread.service.JwtHelper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	private final JwtHelper jwtHelper;
	
	@PostMapping("refresh")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> refresh(@RequestBody Token token) {
		String str = token.getRefreshToken();
		return jwtHelper.refreshToken(str);
	}
	
	@Data
	public static class Token {
		private String refreshToken;
	}
}
