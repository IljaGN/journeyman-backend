package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.dicees.BaseDicePool;
import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

public class DicePoolProperty extends BaseProperty<BaseDicePool> {
  public DicePoolProperty(String name, Value<BaseDicePool> value) {
    super(name, value);
  }
}
