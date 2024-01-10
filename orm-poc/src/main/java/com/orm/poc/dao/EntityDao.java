package com.orm.poc.dao;

import com.orm.poc.domain.exception.OrmPocException;
import com.orm.poc.mapper.EntityMapper;
import com.orm.poc.utils.ReflectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class EntityDao implements Dao {

  private static final String FIND_BY_ID = "SELECT * FROM %s WHERE %s = ?";
  
  private final DataSource dataSource;
  
  private final EntityMapper entityMapper;

  public EntityDao(DataSource dataSource) {
    this.dataSource = dataSource;
    this.entityMapper = new EntityMapper();
  }

  @Override
  public <T, I> Optional<T> findById(Class<T> type, I id) {
    try (Connection connection = this.dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(this.getFormattedQuery(type))) {

      statement.setObject(1, id);
      ResultSet resultSet = statement.executeQuery();

      return resultSet.next() ? Optional.of(this.entityMapper.mapToEntity(resultSet, type)) : Optional.empty();
      
    } catch (Exception e) {
      throw new OrmPocException(
              String.format("Error occurred finding entity [%s] with id [%s]", type.getSimpleName(), id), e);
    }
  }
  
  private <T> String getFormattedQuery(Class<T> type) {
    return FIND_BY_ID.formatted(ReflectionUtils.getTableName(type), ReflectionUtils.getIdName(type));
  }
  
}
