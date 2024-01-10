package com.orm.poc.domain;

public record EntityKey(Class<?> entityType, Object id) {
}
