package ru.gvrn.journeyman.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.observers.api.Info;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class PropertyInfo implements Info {
  public static final PropertyInfo EMPTY_INFO = new PropertyInfo("empty", new Object(), new Object());

  private final String identifier;
  private final Object oldValue;
  private final Object newValue;
}
