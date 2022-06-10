package com.word.spread.model;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.word.spread.model.dto.WordData;

@Entity
@Table(name = "phonetic")
public class Phonetic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String text;
	private URL audio;
	private URL sourceUrl;
	
	
	@ManyToOne
	@JoinColumn(name = "fk_word")
	@JsonIgnore
	private WordData data;
	
	public WordData getData() {
		return data;
	}
	public void setData(WordData data) {
		this.data = data;
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
	public URL getAudio() {
		return audio;
	}
	public void setAudio(URL audio) {
		this.audio = audio;
	}
	public URL getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(URL sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}
