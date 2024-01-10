package com.orm.poc.entity;

import com.orm.poc.annotation.Column;
import com.orm.poc.annotation.Entity;
import com.orm.poc.annotation.Id;
import com.orm.poc.annotation.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "persons")
public class Person {
    
    private final UUID uuid = UUID.randomUUID();
    
    @Id
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    
}
