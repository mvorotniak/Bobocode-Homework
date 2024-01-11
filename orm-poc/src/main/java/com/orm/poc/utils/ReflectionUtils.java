package com.orm.poc.utils;

import com.orm.poc.annotation.Column;
import com.orm.poc.annotation.Id;
import com.orm.poc.annotation.Table;
import com.orm.poc.domain.Pair;
import com.orm.poc.domain.exception.OrmPocException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class ReflectionUtils {
    
    public String getTableName(Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getAnnotation(Table.class))
                .map(Table::name)
                .filter(Predicate.not(String::isEmpty))
                .orElse(entityClass.getSimpleName().toLowerCase());
    }
    
    public String getIdName(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(ReflectionUtils::getColumnName)
                .findFirst()
                .orElseThrow(() -> new OrmPocException(
                        String.format("Unable to get id name for entity [%s]", entityClass.getSimpleName())));
    }
    
    public String getColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::name)
                .filter(Predicate.not(String::isEmpty))
                .orElse(field.getName());
    }
    
    public <T> List<Pair<String, Object>> getFieldNamesToValues(T object) {
        Class<?> type = object.getClass();

        List<Pair<String, Object>> fieldNamesToValues = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                
                Pair<String, Object> fieldNameToValue = buildFieldNameToValue(field, object);
                fieldNamesToValues.add(fieldNameToValue);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new OrmPocException(
                        String.format("Unable to get field names and their values for entity %s", type.getSimpleName()), 
                        e);
            }
        }
        
        return fieldNamesToValues;
    }
    
    private Pair<String, Object> buildFieldNameToValue(Field field, Object object) throws IllegalAccessException {
        String columnName = getColumnName(field);
        Object columnValue = field.get(object);
        
        return Pair.of(columnName, columnValue);
    }
    
}
