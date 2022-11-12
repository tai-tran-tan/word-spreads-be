package com.word.spread.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.word.spread.model.Mean;
import com.word.spread.model.Phonetic;
import com.word.spread.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "word_data")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
@NoArgsConstructor
public class WordData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String word;

	@OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
	private List<Mean> meanings;
	@OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
	private List<Phonetic> phonetics;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	// Hibernate core issue, 
	// this annotation is required to have 
	// creation date updated automatically
	@Column(updatable = false) 
	private Date createdDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "share_user", 
		joinColumns = @JoinColumn(name = "username"), 
		inverseJoinColumns = @JoinColumn(name = "word"))
	private Set<User> sharedUser = new HashSet<>();
	
	public WordData(String word) {
		this.word = word;
	}
}
