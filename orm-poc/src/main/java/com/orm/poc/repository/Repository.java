package com.orm.poc.repository;

import java.util.Optional;

public interface Repository<T, ID> {

  Optional<T> findById(ID id);
  
}
