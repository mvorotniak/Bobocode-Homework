package com.orm.poc.mapper;

import com.orm.poc.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class EntityMapper {
    
    public <T> T mapToEntity(ResultSet resultSet, Class<T> entityClass) throws ReflectiveOperationException, SQLException {
        T entity = entityClass.getDeclaredConstructor().newInstance();

        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = ReflectionUtils.getColumnName(field);
            field.setAccessible(true);
            
            if (Objects.isNull(field.get(entity))) {
                field.set(entity, resultSet.getObject(fieldName, field.getType()));
            }
        }

        return entity;
    }
    
}
