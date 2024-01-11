package com.orm.poc;

import com.orm.poc.configuration.PostgreSQLDatabaseConfiguration;
import com.orm.poc.entity.Person;
import com.orm.poc.session.OrmPocSession;
import com.orm.poc.session.SessionFactory;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrmPocTest {
    
    private SessionFactory sessionFactory;
    
    @BeforeEach
    public void init() {
        DataSource dataSource = PostgreSQLDatabaseConfiguration.getDataSource();
        this.sessionFactory = new SessionFactory(dataSource);
    }
    
    @Order(1)
    @DisplayName("Should find Person by id in database or cache")
    @Test
    void findPersonById() {
        Optional<Person> personOptional1;
        Optional<Person> personOptional2;
        Optional<Person> personOptional3;
        
        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional1 = session.findById(Person.class, 1L);
            personOptional2 = session.findById(Person.class, 1L);
        }

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional3 = session.findById(Person.class, 1L);
        }

        assertThat(personOptional1).isPresent();
        assertThat(personOptional2).isPresent();
        assertThat(personOptional3).isPresent();
        
        Person person1 = personOptional1.get();
        Person person2 = personOptional2.get();
        Person person3 = personOptional3.get();
        assertThat(person1)
                .isEqualTo(person2)
                .isNotEqualTo(person3)
                .isNotNull();
        assertThat(person1.getId()).isEqualTo(1L);
        assertThat(person1.getFirstName()).isEqualTo("First");
        assertThat(person1.getLastName()).isEqualTo("Last");
    }

    @Order(2)
    @DisplayName("Should return Optional.empty when Person not found")
    @Test
    void findPersonById_withNotExistingId() {
        Optional<Person> personOptional;

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional = session.findById(Person.class, 2L);
        }

        assertThat(personOptional).isEmpty();
    }

    @Order(3)
    @DisplayName("Should update entity if changed")
    @Test
    void updatePerson() {
        Optional<Person> personOptional;

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional = session.findById(Person.class, 1L);
            
            personOptional.ifPresent(person -> person.setFirstName("First (Updated)"));
        }

        Optional<Person> personOptional2;

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional2 = session.findById(Person.class, 1L);
        }

        assertThat(personOptional).isPresent();
        assertThat(personOptional2).isPresent();
        
        Person person = personOptional.get();
        Person person2 = personOptional2.get();
        
        assertThat(person.getId())
                .isEqualTo(person2.getId())
                .isEqualTo(1L);
        assertThat(person.getFirstName())
                .isEqualTo(person2.getFirstName())
                .isEqualTo("First (Updated)");
        assertThat(person.getLastName())
                .isEqualTo(person2.getLastName())
                .isEqualTo("Last");
    }

}