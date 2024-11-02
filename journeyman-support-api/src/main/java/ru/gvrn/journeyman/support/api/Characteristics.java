package ru.gvrn.journeyman.support.api;

import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

public interface Characteristics {
  Map<String, Property<?>> getCharacteristics();
}
