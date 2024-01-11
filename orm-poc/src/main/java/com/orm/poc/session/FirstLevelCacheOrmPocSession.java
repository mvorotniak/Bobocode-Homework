package com.orm.poc.session;

import com.orm.poc.dao.Dao;
import com.orm.poc.domain.EntityKey;
import com.orm.poc.domain.Pair;
import com.orm.poc.utils.ReflectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FirstLevelCacheOrmPocSession extends OrmPocSession {
    
    private final Map<EntityKey, Object> cache = new HashMap<>();
    
    private final Map<EntityKey, List<Pair<String, Object>>> snapshots = new HashMap<>();
    
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
                    this.cache.put(entityKey, entity);
                    this.snapshots.put(entityKey, ReflectionUtils.getFieldNamesToValues(entity));
                            
                    return entity;
                });
    }

    @Override
    public void close() {
        this.compareAndUpdateEntities();
        this.clear();
    }

    private void compareAndUpdateEntities() {
        Map<EntityKey, List<Pair<String, Object>>> fieldsToUpdate = this.snapshots.entrySet()
                .stream()
                .peek(entry -> entry.setValue(this.getDiscrepancies(this.cache, entry)))
                .filter(Predicate.not(entry -> entry.getValue().isEmpty()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        fieldsToUpdate.forEach((entityKey, pairs) -> 
                this.entityDao.update(entityKey.entityType(), entityKey.id(), pairs));
    }

    private List<Pair<String, Object>> getDiscrepancies(Map<EntityKey, Object> cache, 
                                     Map.Entry<EntityKey, List<Pair<String, Object>>> entry) {

        List<Pair<String, Object>> snapshotValues = entry.getValue();
        
        return Optional.ofNullable(cache.get(entry.getKey()))
                .map(ReflectionUtils::getFieldNamesToValues)
                .map(cachedFields -> cachedFields.stream()
                        .filter(Predicate.not(snapshotValues::contains))
                        .toList())
                .orElse(Collections.emptyList());
    }

    private void clear() {
        this.cache.clear();
        this.snapshots.clear();
    }
}
