package ru.gvrn.journeyman.properties.values;

import lombok.Getter;
import lombok.Setter;
import ru.gvrn.journeyman.observers.SupportObservable;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.properties.PropertyInfo;
import ru.gvrn.journeyman.properties.api.Value;

import java.util.Objects;

public class PropertyValue<T> extends SupportObservable implements Value<T> {
  @Getter
  protected final String name;

  @Setter
  protected String ownerName = "null";

  @Getter
  private T value;

  public PropertyValue(String name, T value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public void setValue(T value) {
    if (Objects.equals(this.value, value)) {
      return;
    }

    Info info = new PropertyInfo(getPropertyIdentifier(), this.value, value);
    this.value = value;
    notify(info);
  }

  @Override
  public Value<T> getInstance(String valueName) {
    Value<T> pv = new PropertyValue<>(valueName, value);
    pv.setOwnerName(ownerName);
    return pv;
  }

  protected String getPropertyIdentifier() {
    return String.format("%s(%s)", ownerName, name);
  }
}
