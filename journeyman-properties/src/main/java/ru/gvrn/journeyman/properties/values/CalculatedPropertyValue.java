package ru.gvrn.journeyman.properties.values;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.properties.PropertyInfo;

import java.util.Objects;
import java.util.function.Function;

public class CalculatedPropertyValue<T> extends PropertyValue<T> implements Observer {
//  private Boolean isObservePropertys; // Если сетится value то отключается, когда переводится в true пересчитыает value

  private Function<Info, T> dependencies;

  public CalculatedPropertyValue(String name, T value) {
    super(name, value);
  }


  public void setDependenciesAndUpdate(@NotNull Function<Info, T> dependencies) {
    setDependencies(dependencies);
    update(PropertyInfo.EMPTY_INFO);
  }

  public void setDependencies(@NonNull Function<Info, T> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public void update(@NonNull Info info) {
    if (Objects.isNull(dependencies)) {
      throw new IllegalStateException(String.format("Property value '%s' cannot be update. " +
              "The observed property '%s' has changed, but dependencies is null",
          getPropertyIdentifier(), info.getDisplayedIdentifier()));
    }
    setValue(dependencies.apply(info));
  }
}
