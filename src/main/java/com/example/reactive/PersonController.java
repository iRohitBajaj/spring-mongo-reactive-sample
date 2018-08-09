package com.example.reactive;

import com.example.reactive.model.Person;
import com.example.reactive.repository.PersonMongoRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/persons")
class PersonController {

    private final PersonMongoRepository personrepo;

    public PersonController(PersonMongoRepository personrepo) {
        this.personrepo = personrepo;
    }

    @GetMapping("")
    public Flux<Person> all() {
        return this.personrepo.findAll();
    }

    @PostMapping("")
    public Mono<Person> create(@RequestBody Person person) {
        return this.personrepo.save(person);
    }

    @GetMapping("/{id}")
    public Mono<Person> get(@PathVariable("id") String id) {
        return this.personrepo.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<Person> update(@PathVariable("id") String id, @RequestBody Person person) {
        return this.personrepo.findById(id)
                .map(p -> {
                    p.setName(person.getName());
                    p.setHeight(person.getHeight());
                    return p;
                })
                .flatMap(p -> this.personrepo.save(p));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return this.personrepo.deleteById(id);
    }

}
