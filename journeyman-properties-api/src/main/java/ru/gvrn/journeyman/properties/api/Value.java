package ru.gvrn.journeyman.properties.api;

public interface Value<T> {
  T getValue();
  void setValue(T value);
  Value<T> getInstance(String valueName);
  void setOwnerName(String name);
}
