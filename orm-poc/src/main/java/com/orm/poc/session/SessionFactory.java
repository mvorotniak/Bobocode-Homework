package com.orm.poc.session;

import com.orm.poc.dao.Dao;
import com.orm.poc.dao.EntityDao;

import javax.sql.DataSource;

public class SessionFactory {

    private final Dao entityDao;

    public SessionFactory(DataSource dataSource) {
        this.entityDao = new EntityDao(dataSource);
    }

    public OrmPocSession openSession() {
        return new FirstLevelCacheOrmPocSession(this.entityDao);
    }
    
}
