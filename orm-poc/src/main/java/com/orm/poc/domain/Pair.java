package com.orm.poc.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Pair<L, R> {
    
    private final L left;
    
    private final R right;
    
    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
    
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
