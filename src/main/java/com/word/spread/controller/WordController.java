package com.word.spread.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/words", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class WordController {

	private WordDataService service;
	private WordDataRepository repo;
	
	public WordController(WordDataService svc, WordDataRepository repo) {
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
	public List<WordData> getRecent() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "updatedDate"));
		return this.repo.findAll(pageRequest);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addWord(@RequestBody ShareForm form) {
		WordData data = get(form.getWord());
		this.repo.save(data);
	}
	
	@Data
	@NoArgsConstructor
	public static class ShareForm implements Serializable{
		private static final long serialVersionUID = 1L;
		private String word;
	}
}
