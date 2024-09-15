package ru.gvrn.journeyman.properties.api;

import ru.gvrn.journeyman.outfits.api.UniqueName;

public interface Property<T> extends UniqueName {
  T getValue();
  void replaceValue(T value);
  void resetOnDefault();
  void updateDefaultOnCurrent();
//    void accept(Visitor visitor);
}
