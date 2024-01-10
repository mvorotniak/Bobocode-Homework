package com.orm.poc.session;

import com.orm.poc.dao.Dao;
import com.orm.poc.domain.EntityKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FirstLevelCacheOrmPocSession extends OrmPocSession {
    
    private final Map<EntityKey, Object> cache = new HashMap<>();
    
    public FirstLevelCacheOrmPocSession(Dao entityDao) {
        super(entityDao);
    }

    @Override
    public <T, I> Optional<T> findById(Class<T> type, I id) {
        EntityKey entityKey = new EntityKey(type, id);

        return Optional.ofNullable((T) cache.get(entityKey))
                .or(() -> this.findEntityAndPopulateCache(entityKey, type, id));
    }
    
    private <T> Optional<T> findEntityAndPopulateCache(EntityKey entityKey, Class<T> type, Object id) {
        return this.entityDao.findById(type, id)
                .map(entity -> {
                    cache.put(entityKey, entity);
                    return entity;
                });
    }

    @Override
    public void close() {
        cache.clear();
    }
}
