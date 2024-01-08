package com.orm.poc.utils;

import com.orm.poc.annotation.Column;
import com.orm.poc.annotation.Id;
import com.orm.poc.annotation.Table;
import com.orm.poc.exception.OrmPocException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
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
    
}
