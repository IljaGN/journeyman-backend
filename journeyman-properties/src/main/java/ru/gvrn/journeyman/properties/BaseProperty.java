package ru.gvrn.journeyman.properties;

import lombok.Getter;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.api.Value;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseProperty<T> implements Property<T> {
  @Getter // TODO: interface UniqueName
  private final String name;
  protected final Map<String, Value<T>> values;
  protected final Value<T> defolt;
  protected final Value<T> current;

  public BaseProperty(String name, Value<T> value) {
    this.name = name;
    value.setOwnerName(name);
    values = new HashMap<>();
    current = value;
    defolt = initValue("default");
    addValues(current, defolt);
  }

  @Override
  public T getValue() {
    return current.getValue();
  }

  @Override
  public void replaceValue(T value) {
    current.setValue(value);
  }

  @Override
  public void resetOnDefault() {
    current.setValue(defolt.getValue());
  }

  @Override
  public void updateDefaultOnCurrent() {
    defolt.setValue(current.getValue());
  }

  protected Value<T> initValue(String name) {
    return current.getInstance(name);
  }

  protected void addValues(Value<T>... values) {
//    Arrays.stream(values).forEach(v -> this.values.put(v.getName(), v));
  }
}
