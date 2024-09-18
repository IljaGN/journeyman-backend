package ru.gvrn.journeyman.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PropertyAndValueDefinition<T> {
  @Getter
  private final String name;
  @Getter
  private final Class<?> type;
  private final Map<String, PropertyValue<T>> nameValueMap = new HashMap<>();

  public PropertyValue<T> getValue() {
    return nameValueMap.get("current");
  }

  public PropertyValue<T> getValue(String valueName) {
    return nameValueMap.get(valueName);
  }

  public Collection<PropertyValue<T>> getValues() {
    return new ArrayList<>(nameValueMap.values());
  }

  public void setValue(PropertyValue<T> value) {
    nameValueMap.put(value.getName(), value);
  }
}
