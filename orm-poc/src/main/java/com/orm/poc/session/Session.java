package com.orm.poc.session;

import java.io.Closeable;
import java.util.Optional;

public interface Session extends Closeable {
    
    <T, I> Optional<T> findById(Class<T> type, I id);
    
}
