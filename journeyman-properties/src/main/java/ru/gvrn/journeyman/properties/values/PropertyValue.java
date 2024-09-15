package ru.gvrn.journeyman.properties.values;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observable;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.properties.PropertyInfo;
import ru.gvrn.journeyman.properties.api.Value;

import java.util.ArrayList;
import java.util.List;

public class PropertyValue<T> implements Value<T>, Observable {
  private final List<Observer> observers = new ArrayList<>();

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

  @Override
  public void add(@NonNull Observer observer) {
    observers.add(observer);
  }

  @Override
  public void delete(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void deleteObservers() {
    observers.clear();
  }

  @Override
  public void notify(@NonNull Info info) {
    observers.forEach(obs -> obs.update(info));
  }

  protected String getPropertyIdentifier() {
    return String.format("%s(%s)", ownerName, name);
  }
}
