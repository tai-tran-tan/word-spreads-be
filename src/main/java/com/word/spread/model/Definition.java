package com.word.spread.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "definition")
public class Definition implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@JsonProperty("definition")
	private String text;
	
	@ManyToOne
	@JoinColumn(name = "mean_id")
	@JsonIgnore
	private Mean mean;

//	@ElementCollection
//	private List<String> synonyms;
//	@ElementCollection
//	private List<String> antonyms;

	public Mean getMean() {
		return mean;
	}
	public void setMean(Mean mean) {
		this.mean = mean;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
//	public List<String> getSynonyms() {
//		return synonyms;
//	}
//	public void setSynonyms(List<String> synonyms) {
//		this.synonyms = synonyms;
//	}
//	public List<String> getAntonyms() {
//		return antonyms;
//	}
//	public void setAntonyms(List<String> antonyms) {
//		this.antonyms = antonyms;
//	}
}
