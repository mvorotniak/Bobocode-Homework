package com.orm.poc.repository;

import com.orm.poc.configuration.DatabaseConfiguration;
import com.orm.poc.exception.OrmPocException;
import com.orm.poc.mapper.EntityMapper;
import com.orm.poc.utils.ReflectionUtils;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class EntityRepository<T, ID> implements Repository<T, ID> {

  private static final String FIND_BY_ID = "SELECT * FROM %s WHERE %s = %s";
  
  private final DataSource dataSource;
  
  private final EntityMapper<T> entityMapper;
  
  private final Class<T> type;

  public EntityRepository(Class<T> type) {
    this.dataSource = this.initDataSource();
    this.entityMapper = new EntityMapper<>();
    this.type = type;
  }

  @Override
  public Optional<T> findById(ID id) {
    try (Connection connection = this.dataSource.getConnection(); 
         Statement statement = connection.createStatement()) {
      
      ResultSet resultSet = this.executeStatement(statement, id);

      return resultSet.next() ? Optional.of(this.entityMapper.mapToEntity(resultSet, this.type)) : Optional.empty();
      
    } catch (Exception e) {
      throw new OrmPocException(String.format("Error occurred finding entity with id [%s]", id), e);
    }
  }
  
  private ResultSet executeStatement(Statement statement, Object id) throws SQLException {
    String query = String.format(FIND_BY_ID, 
            ReflectionUtils.getTableName(this.type), 
            ReflectionUtils.getIdName(this.type), 
            id);
    
    return statement.executeQuery(query);
  }
  
  private DataSource initDataSource() {
    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
    pgSimpleDataSource.setUser(DatabaseConfiguration.USERNAME);
    pgSimpleDataSource.setPassword(DatabaseConfiguration.PASSWORD);
    pgSimpleDataSource.setUrl(DatabaseConfiguration.URL);
    
    return pgSimpleDataSource;
  }
  
}
