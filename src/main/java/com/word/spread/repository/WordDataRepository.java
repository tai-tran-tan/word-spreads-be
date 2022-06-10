package com.word.spread.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.word.spread.model.dto.WordData;

@Repository
public interface WordDataRepository extends CrudRepository<WordData, String>{
	
}
