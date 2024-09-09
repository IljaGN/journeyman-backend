package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

public class IntegerProperty extends BaseProperty<Integer> {
  public IntegerProperty(String name, Value<Integer> value) {
    super(name, value);
  }
}
