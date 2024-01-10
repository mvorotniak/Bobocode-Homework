package com.orm.poc.session;

import com.orm.poc.dao.Dao;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class OrmPocSession implements Session {
    
    final Dao entityDao;
    
    @Override
    public <T, I> Optional<T> findById(Class<T> type, I id) {
        return entityDao.findById(type, id);
    }

    @Override
    public void close() throws IOException {
        // Do nothing
    }
}
