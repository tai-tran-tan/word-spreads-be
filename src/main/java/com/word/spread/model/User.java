package com.word.spread.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.word.spread.model.dto.WordData;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	@JsonIgnore
	private String password;
	
	@ManyToMany(mappedBy = "sharedUser")
	private Set<WordData> sharedWord;

	public User(String name) {
		this.username = name;
	}

	@ElementCollection
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = javax.persistence.CascadeType.ALL, targetEntity = Role.class)
	private Collection<Role> roles = new ArrayList<>(Arrays.asList(new Role("USER")));

	@Override
	public Collection<Role> getAuthorities() {
		return this.roles;
	}

	public void setAuthorities(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
