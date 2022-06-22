package com.word.spread.repository;

import org.springframework.data.repository.CrudRepository;

import com.word.spread.model.Role;

public interface RoleRepo extends CrudRepository<Role, String> {
	Role findByAuthority(String authority);
}
