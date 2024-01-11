package com.orm.poc.dao;

import com.orm.poc.domain.Pair;
import com.orm.poc.domain.exception.OrmPocException;
import com.orm.poc.mapper.EntityMapper;
import com.orm.poc.utils.ReflectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.orm.poc.utils.QueryStrings.*;

public class EntityDao implements Dao {
  
  private final DataSource dataSource;
  
  private final EntityMapper entityMapper;

  public EntityDao(DataSource dataSource) {
    this.dataSource = dataSource;
    this.entityMapper = new EntityMapper();
  }

  @Override
  public <T, I> Optional<T> findById(Class<T> type, I id) {
    try (Connection connection = this.dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(this.getFindByIdQuery(type))) {

      this.populatePreparedStatement(statement, id);
      ResultSet resultSet = statement.executeQuery();

      return resultSet.next() ? Optional.of(this.entityMapper.mapToEntity(resultSet, type)) : Optional.empty();
      
    } catch (Exception e) {
      throw new OrmPocException(
              String.format("Error occurred finding entity [%s] with id [%s]", type.getSimpleName(), id), e);
    }
  }

  @Override
  public <T, I> void update(Class<T> type, I id, List<Pair<String, Object>> fieldNameToValuePairs) {
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(this.getUpdateQuery(type, fieldNameToValuePairs))) {

      this.populatePreparedStatement(statement, id, fieldNameToValuePairs);
      statement.executeUpdate();
      
    } catch (Exception e) {
      throw new OrmPocException(
              String.format("Error occurred updating entity [%s] with id [%s]", type.getSimpleName(), id), e);
    }
  }

  private <T> String getFindByIdQuery(Class<T> type) {
    return FIND_BY_ID_QUERY.formatted(ReflectionUtils.getTableName(type), ReflectionUtils.getIdName(type));
  }
  
  private <T> String getUpdateQuery(Class<T> type, List<Pair<String, Object>> fieldNameToValuePairs) {
    String setValues = fieldNameToValuePairs
            .stream()
            .map(pair -> SET_VALUE.formatted(pair.getLeft()))
            .collect(Collectors.joining(","));
    
    return UPDATE_QUERY.formatted(ReflectionUtils.getTableName(type), setValues, ReflectionUtils.getIdName(type));
  }

  private void populatePreparedStatement(PreparedStatement statement, Object id) throws SQLException {
    this.populatePreparedStatement(statement, id, Collections.emptyList());
  }
  
  private void populatePreparedStatement(PreparedStatement statement, 
                                         Object id, 
                                         List<Pair<String, Object>> fieldNameToValuePairs) throws SQLException {
    int i;
    for (i = 1; i <= fieldNameToValuePairs.size(); i++) {
      statement.setObject(i, fieldNameToValuePairs.get(i - 1).getRight());
    }
    statement.setObject(i, id);
  }
  
}
