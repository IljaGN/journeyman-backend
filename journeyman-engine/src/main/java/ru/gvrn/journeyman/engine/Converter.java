package ru.gvrn.journeyman.engine;

public interface Converter<T, R> {
  R convert(T obj);
}
