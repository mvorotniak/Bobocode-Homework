package com.orm.poc;

import com.orm.poc.configuration.PostgreSQLDatabaseConfiguration;
import com.orm.poc.entity.Person;
import com.orm.poc.session.OrmPocSession;
import com.orm.poc.session.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrmPocTest {
    
    private SessionFactory sessionFactory;
    
    @BeforeEach
    public void init() {
        DataSource dataSource = PostgreSQLDatabaseConfiguration.getDataSource();
        this.sessionFactory = new SessionFactory(dataSource);
    }
    
    @DisplayName("Should find Person by id in database or cache")
    @Test
    void findPersonById() {
        Optional<Person> personOptional1 = Optional.empty();
        Optional<Person> personOptional2 = Optional.empty();
        Optional<Person> personOptional3 = Optional.empty();
        
        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional1 = session.findById(Person.class, 1L);
            personOptional2 = session.findById(Person.class, 1L);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional3 = session.findById(Person.class, 1L);
        } catch (IOException e) {
            e.printStackTrace();
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

    @DisplayName("Should return Optional.empty when Person not found")
    @Test
    void findPersonById_withNotExistingId() {
        Optional<Person> personOptional;

        try (OrmPocSession session = this.sessionFactory.openSession()) {
            personOptional = session.findById(Person.class, 2L);
        } catch (IOException e) {
            personOptional = Optional.empty();
        }

        assertThat(personOptional).isEmpty();
    }

}