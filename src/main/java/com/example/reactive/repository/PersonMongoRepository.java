package com.example.reactive.repository;

import com.example.reactive.model.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonMongoRepository extends ReactiveMongoRepository<Person, String> {
}
