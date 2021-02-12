package com.example.spring.converters;

public interface BaseConverter<O, T> {

    T convert(O original);
}
