package com.word.spread.service;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.word.spread.model.Role;
import com.word.spread.model.User;
import com.word.spread.repository.RoleRepo;
import com.word.spread.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {
	
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username);
	}

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepo.save(role);
	}

	@Override
	public User addRoleToUser(String user, String role) {
		Role authority = roleRepo.findByAuthority(role);
		User userByUsername = this.loadUserByUsername(user);
		userByUsername.getAuthorities().add(authority);
		return userByUsername;
	}

	@Override
	public User getUser(String name) {
		return loadUserByUsername(name);
	}

	@Override
	public Iterable<User> getUsers() {
		return userRepo.findAll();
	}

}
