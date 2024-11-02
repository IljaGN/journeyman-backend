package ru.gvrn.journeyman.properties.api;

import ru.gvrn.journeyman.support.api.UniqueName;

public interface Property<T> extends UniqueName {
  T getValue();
  void replaceValue(T value);
  void resetOnDefault();
  void updateDefaultOnCurrent();
//    void accept(Visitor visitor);
}
