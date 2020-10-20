package com.spring.boot.example.core.mapping;

public interface Mapper<T, R> {

    R map(T value);

}
