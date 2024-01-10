package com.orm.poc.dao;

import java.util.Optional;

public interface Dao {

  <T, I> Optional<T> findById(Class<T> type, I id);
  
}
