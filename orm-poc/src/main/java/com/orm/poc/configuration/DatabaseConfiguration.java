package com.orm.poc.configuration;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class DatabaseConfiguration {
    
    public final String USERNAME = "postgres";
    
    public final String PASSWORD = "postgres";
    
    public final String URL = "jdbc:postgresql://localhost:5432/homework";
    
}
