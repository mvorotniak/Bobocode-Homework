package com.orm.poc.mapper;

import com.orm.poc.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class EntityMapper<T> {
    
    public T mapToEntity(ResultSet resultSet, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();

        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = ReflectionUtils.getColumnName(field);
            field.setAccessible(true);
            field.set(entity, resultSet.getObject(fieldName, field.getType()));
        }

        return entity;
    }
    
}
