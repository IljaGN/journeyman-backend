package ru.gvrn.journeyman.properties.api;

import ru.gvrn.journeyman.outfits.api.UniqueName;

public interface Value<T> extends UniqueName {
  T getValue();
  void setValue(T value);
  Value<T> getInstance(String valueName);
  void setOwnerName(String name);
}
