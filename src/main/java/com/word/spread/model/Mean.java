package com.word.spread.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.word.spread.model.dto.WordData;

@Entity
@Table(name = "mean")
public class Mean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	private String partOfSpeech;

	@OneToMany(mappedBy = "mean", cascade = CascadeType.ALL)
	private List<Definition> definitions;
//	@OneToMany(mappedBy = "mean", cascade = CascadeType.ALL)
//	private List<Phonetic> phonetics;

	@ManyToOne
	@JoinColumn(name = "fk_word")
	@JsonIgnore
	private WordData data;

//	@ElementCollection
//	private List<String> synonyms;
//	@ElementCollection
//	private List<String> antonyms;

	public WordData getData() {
		return data;
	}

	public void setData(WordData data) {
		this.data = data;
	}

	// public List<Phonetic> getPhonetics() {
//		return phonetics;
//	}
//	public void setPhonetics(List<Phonetic> phonetics) {
//		this.phonetics = phonetics;
//	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
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
