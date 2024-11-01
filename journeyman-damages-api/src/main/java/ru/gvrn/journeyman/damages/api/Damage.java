package ru.gvrn.journeyman.damages.api;

import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

public interface Damage {
  default void cause(Map<String, Property<?>> characteristics) {
    cause(characteristics, 1);
  }

  default void causeCritical(Map<String, Property<?>> characteristics) {
    causeCritical(characteristics, 1);
  }

  boolean isCritical(int naturalD20Value);
  void cause(Map<String, Property<?>> characteristics, int times);
  void causeCritical(Map<String, Property<?>> characteristics, int times);
}
