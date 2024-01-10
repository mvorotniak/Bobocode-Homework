package com.orm.poc.repository;

import entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EntityRepositoryTest {
    
    private Repository<Person, Long> personRepository;
    
    @BeforeEach
    public void init() {
        this.personRepository = new EntityRepository<>(Person.class);
    }
    
    @DisplayName("Should find Person by id")
    @Test
    void findPersonById() {
        Optional<Person> personOptional = this.personRepository.findById(1L);
        
        assertThat(personOptional).isPresent();
        
        Person person = personOptional.get();
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getFirstName()).isEqualTo("First");
        assertThat(person.getLastName()).isEqualTo("Last");
    }

    @DisplayName("Should return Optional.empty when Person not found")
    @Test
    void findPersonById_withNotExistingId() {
        Optional<Person> personOptional = this.personRepository.findById(2L);

        assertThat(personOptional).isEmpty();
    }

}