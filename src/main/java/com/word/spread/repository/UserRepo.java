package com.word.spread.repository;

import org.springframework.data.repository.CrudRepository;

import com.word.spread.model.User;

public interface UserRepo extends CrudRepository<User, String> {
	User findByUsername(String username);
}
