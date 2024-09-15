package ru.gvrn.journeyman.properties;

import lombok.Getter;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.api.Value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseProperty<T> implements Property<T> {
  @Getter
  protected final String name;
  protected final Map<String, Value<T>> values = new HashMap<>();
  protected final Value<T> defolt;
  protected final Value<T> current;

  @SafeVarargs
  public BaseProperty(String name, Value<T> currentValue, Value<T>... values) {
    this.name = name;
    currentValue.setOwnerName(name);
    Arrays.stream(values).forEach(v -> v.setOwnerName(name));
    current = currentValue;
    if (values.length == 0) {
      defolt = initValue("default");
      addValues(current, defolt);
    } else {
      defolt = values[0];
      addValues(current);
      addValues(values);
    }
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

  @SafeVarargs
  protected final void addValues(Value<T>... values) {
    Arrays.stream(values).forEach(v -> this.values.put(v.getName(), v));
  }
}
