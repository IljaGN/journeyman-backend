package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

// цеопочка наследования должна быть прямой для Visitor
public class IntegerProperty extends BaseProperty<Integer> {
  public IntegerProperty(String name, Value<Integer> value) {
    super(name, value);
  }

  public IntegerProperty(String name, Value<Integer> current, Value<Integer> defolt) {
    super(name, current, defolt);
  }
}
