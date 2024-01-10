package com.orm.poc.configuration;

import lombok.experimental.UtilityClass;
import org.postgresql.ds.PGSimpleDataSource;

@UtilityClass
public final class PostgreSQLDatabaseConfiguration {
    
    private final String USERNAME = "postgres";
    
    private final String PASSWORD = "postgres";
    
    private final String URL = "jdbc:postgresql://localhost:5432/homework";

    public PGSimpleDataSource getDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser(USERNAME);
        pgSimpleDataSource.setPassword(PASSWORD);
        pgSimpleDataSource.setUrl(URL);

        return pgSimpleDataSource;
    }
    
}
