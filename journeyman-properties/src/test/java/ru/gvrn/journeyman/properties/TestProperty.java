package ru.gvrn.journeyman.properties;

import ru.gvrn.journeyman.properties.api.Value;

public class TestProperty extends BaseProperty<Object> {
  @SafeVarargs
  public TestProperty(String name, Value<Object> currentValue, Value<Object>... values) {
    super(name, currentValue, values);
  }
}
