package com.word.spread.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.word.spread.model.Role;
import com.word.spread.model.User;

public interface UserService extends UserDetailsService {
	User saveUser(User user);
	Role saveRole(Role role);
	User addRoleToUser(String user, String role);
	User getUser(String name);
	Iterable<User> getUsers();
}
