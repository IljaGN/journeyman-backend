package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

public class StringProperty extends BaseProperty<String> {
  public StringProperty(String name, Value<String> value) {
    super(name, value);
  }
}
