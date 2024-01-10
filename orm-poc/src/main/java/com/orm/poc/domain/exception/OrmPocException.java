package com.orm.poc.domain.exception;

public class OrmPocException extends RuntimeException {
    
    public OrmPocException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrmPocException(String message) {
        super(message);
    }
}
