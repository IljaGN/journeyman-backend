package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

public class BooleanProperty extends BaseProperty<Boolean> {
  public BooleanProperty(String name, Value<Boolean> value) {
    super(name, value);
  }
}
