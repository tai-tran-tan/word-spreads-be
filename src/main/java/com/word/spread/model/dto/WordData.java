package com.word.spread.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.word.spread.model.Mean;
import com.word.spread.model.Phonetic;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "word_data")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
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
	@Column(updatable = false)
	private Date createdDate;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	public WordData(String word) {
		this.word = word;
	}
	
	public WordData() {}
}
