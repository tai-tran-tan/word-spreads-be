package com.word.spread.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.word.spread.model.dto.WordData;
import com.word.spread.repository.WordDataRepository;
import com.word.spread.service.WordDataService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/words", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(originPatterns = "*")
@Slf4j
public class WordResource {

	private WordDataService service;
	private WordDataRepository repo;
	
	public WordResource(WordDataService svc, WordDataRepository repo) {
		this.service = svc;
		this.repo = repo;
	}
	
	@GetMapping(path = "/{word}")
	public WordData getWordData(@PathVariable(name = "word") String word) {
		return get(word);
	}

	private WordData get(String word) {
		return this.repo.findById(word).orElseGet(() -> getAndPersistWord(word));
	}

	private WordData getAndPersistWord(String word) {
		log.info("Word is not existing in DB, fetching online then store...");
		WordData data = service.getWordData(word);
		repo.save(data);
		return data;
	}
	
	@GetMapping
	public Iterable<WordData> getRecent() {
		return this.repo.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addWord(@RequestBody String word) {
		WordData data = get(word);
		this.repo.save(data);
	}
}
