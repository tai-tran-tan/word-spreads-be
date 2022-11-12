package com.word.spread.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.word.spread.model.User;
import com.word.spread.model.dto.WordData;
import com.word.spread.repository.WordDataRepository;
import com.word.spread.service.WordDataService;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/words", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class WordController {

	private final WordDataService service;
	private final WordDataRepository repo;
	
	@GetMapping(path = "/{word}")
	public WordData getWordData(@PathVariable(name = "word") String word) {
		return get(word);
	}

	@GetMapping
	public List<WordData> getRecent() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "updatedDate"));
		return this.repo.findAll(pageRequest);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addWord(@RequestBody ShareForm form, Authentication auth) {
		String word = form.getWord();
		User user = extractUserDetails(auth);
		share(word, user);
	}

	@PutMapping(path = "/{word}")
	@ResponseStatus(HttpStatus.OK)
	public WordData coShare(@PathVariable(name = "word") String word, Authentication auth) {
		User user = extractUserDetails(auth);
		return share(word, user);
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
	
	private WordData share(String word, User user) {
		WordData data = get(word);
		Set<User> users = SetUtils.hashSet(user);
		users.addAll(data.getSharedUser());
		data.setSharedUser(users);
		return this.repo.save(data);
	}

	private User extractUserDetails(Authentication auth) {
		return (User) auth.getDetails();
	}
	
	@Data
	@NoArgsConstructor
	public static class ShareForm implements Serializable{
		private static final long serialVersionUID = 1L;
		private String word;
	}
}
