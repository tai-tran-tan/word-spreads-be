package com.word.spread.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.word.spread.security.filter.CustomizedAuthenticationFilter;
import com.word.spread.security.filter.JwtRequestFilter;
import com.word.spread.service.JwtHelper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig {

	private final UserDetailsService userService;
	private final AuthenticationConfiguration configuration;
	private final JwtHelper jwtHelper;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		CustomizedAuthenticationFilter filter = new CustomizedAuthenticationFilter(authenticationManager(), jwtHelper);
		filter.setFilterProcessesUrl("/api/login");
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/login").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/words").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(filter);
		http.addFilterBefore(new JwtRequestFilter(jwtHelper), CustomizedAuthenticationFilter.class);
		http.cors();
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return configuration.getAuthenticationManager();
	}

	//temporary disable cors
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Autowired
	void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
