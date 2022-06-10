package com.word.spread.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.word.spread.model.Mean;
import com.word.spread.model.Phonetic;
import com.word.spread.model.dto.WordData;

@Service
public class WordDataService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WordDataService.class);
	private RestTemplate client;
	
	public WordDataService() {
		this.client = new RestTemplate();
	}

	public WordData getWordData(String word) {
		LOGGER.info("Fetching info: " + word);
		RequestEntity<Void> request = RequestEntity.get("https://api.dictionaryapi.dev/api/v2/entries/en/{word}", word)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		ParameterizedTypeReference<List<WordData>> type = new ParameterizedTypeReference<>() {};
		
		try {
			ResponseEntity<List<WordData>> entity = client.exchange(request, type);
			List<WordData> body = entity.getBody();
			WordData wd = new WordData(word);

			return body.stream().reduce(wd, (a, b) -> {
				List<Mean> meanings = union(a.getMeanings(), b.getMeanings());
				List<Phonetic> phonetics = union(a.getPhonetics(), b.getPhonetics());
				
				//reverse reference for foreign key. TOBE smarter
				meanings.forEach(m -> {
					m.setData(wd);
					m.getDefinitions().forEach(d -> d.setMean(m));
				});
				phonetics.forEach(p -> p.setData(wd));
				
				wd.setMeanings(meanings);
				wd.setPhonetics(phonetics);
				return wd;
			});
		} catch (RestClientException e) {
			LOGGER.error("REST client exception", e);
			return null;
		}
	}
	
	private <T> List<T> union(List<T> a, List<T> b) {
		return Stream.concat(createStream(a), createStream(b))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private <T> Stream<T> createStream(List<T> list) {
		if (list != null) {
			return list.stream();
		}
		return Stream.empty();
	}

}
