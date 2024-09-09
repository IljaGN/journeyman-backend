package ru.gvrn.journeyman.properties.values;

import lombok.Getter;
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

  @Getter // TODO: interface UniqueName
  private final String name;

  @Setter
  private String ownerName;

  @Getter
  private T value;

  public PropertyValue(String name, T value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public void setValue(T value) {
    Info info = new PropertyInfo(ownerName, this.value, value);
    this.value = value;
    notify(info);
  }

  @Override
  public Value<T> getInstance(String valueName) {
    return null;
  }

  @Override
  public void add(Observer observer) {
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
  public void notify(Info info) {
    observers.forEach(obs -> obs.update(info));
  }
}
