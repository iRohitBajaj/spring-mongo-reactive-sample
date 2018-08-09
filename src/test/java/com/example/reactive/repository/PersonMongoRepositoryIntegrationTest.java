package com.example.reactive.repository;

import com.example.reactive.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        //classes = {SpringMongoReactiveApplication.class, EmbeddedMongoTestConfig.class})
@ContextConfiguration(classes={EmbeddedMongoTestConfig.class})
public class PersonMongoRepositoryIntegrationTest {

    @Autowired
    PersonMongoRepository repository;

    @Test
    public void givenExample_whenFindAllWithExample_thenFindAllMacthings() {
        repository.save(new Person(null, "john", 12.3)).block();
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", startsWith());
        Example<Person> example = Example.of(new Person(null, "jo", null), matcher);
        Flux<Person> accountFlux = repository.findAll(example);

        StepVerifier
                .create(accountFlux)
                .assertNext(person -> assertEquals("john", person.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void givenAccount_whenSave_thenSave() {
        Mono<Person> accountMono = repository.save(new Person(null, "john", 12.3));

        StepVerifier
                .create(accountMono)
                .assertNext(person -> assertNotNull(person.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    public void givenId_whenFindById_thenFindAccount() {
        Person inserted = repository.save(new Person(null, "john", 12.3)).block();
        Mono<Person> accountMono = repository.findById(inserted.getId());

        StepVerifier
                .create(accountMono)
                .assertNext(person -> {
                    assertEquals("john", person.getName());
                    assertEquals(Double.valueOf(12.3),  person.getHeight());
                    assertNotNull(person.getId());
                })
                .expectComplete()
                .verify();
    }
}