package com.word.spread.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.word.spread.model.Mean;
import com.word.spread.model.Phonetic;

@Entity
@Table(name = "word_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WordData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String word;

	@OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
	private List<Mean> meanings;
	@OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
	private List<Phonetic> phonetics;
	
	public WordData(String word) {
		this.word = word;
	}
	
	public WordData() {}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public List<Mean> getMeanings() {
		return meanings;
	}
	public void setMeanings(List<Mean> meanings) {
		this.meanings = meanings;
	}
	public List<Phonetic> getPhonetics() {
		return phonetics;
	}
	public void setPhonetics(List<Phonetic> phonetics) {
		this.phonetics = phonetics;
	}
}
