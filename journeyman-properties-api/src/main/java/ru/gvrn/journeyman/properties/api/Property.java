package ru.gvrn.journeyman.properties.api;

public interface Property<T> {
  T getValue();
  void replaceValue(T value);
  void resetOnDefault();
  void updateDefaultOnCurrent();
//    void accept(Visitor visitor);
}
