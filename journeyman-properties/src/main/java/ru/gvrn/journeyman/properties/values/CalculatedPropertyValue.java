package ru.gvrn.journeyman.properties.values;

import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.properties.PropertyInfo;

import java.util.function.Function;

public class CalculatedPropertyValue<T> extends PropertyValue<T> implements Observer {
//  private Boolean isObservePropertys; // Если сетится value то отключается, когда переводится в true пересчитыает value

  private Function<Info, T> dependencies;

  public CalculatedPropertyValue(String name, T value) {
    super(name, value);
  }

  public void setDependenciesAndUpdate(Function<Info, T> dependencies) {
    setDependencies(dependencies);
    update(PropertyInfo.EMPTY_INFO);
  }

  public void setDependencies(Function<Info, T> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public void update(Info info) {
    setValue(dependencies.apply(info));
  }
}
