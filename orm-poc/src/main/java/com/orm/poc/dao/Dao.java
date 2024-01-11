package com.orm.poc.dao;

import com.orm.poc.domain.Pair;

import java.util.List;
import java.util.Optional;

public interface Dao {

  <T, I> Optional<T> findById(Class<T> type, I id);

  <T, I> void update(Class<T> type, I id, List<Pair<String, Object>> fieldNameToValuePairs);
  
}
