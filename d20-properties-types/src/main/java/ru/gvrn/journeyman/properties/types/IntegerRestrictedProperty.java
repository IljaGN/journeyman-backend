package ru.gvrn.journeyman.properties.types;

import ru.gvrn.journeyman.properties.BaseProperty;
import ru.gvrn.journeyman.properties.api.Value;

public class IntegerRestrictedProperty extends BaseProperty<Integer> {
  public IntegerRestrictedProperty(String name, Value<Integer> current, Value<Integer> min, Value<Integer> max) {
    super(
        name, current, max, max.getInstance("default_max"),
        min, min.getInstance("default_min")
    );
  }

  private void validateMaxMinValue() {

  }

  // Возможно нужно будет изменить все методы
}
