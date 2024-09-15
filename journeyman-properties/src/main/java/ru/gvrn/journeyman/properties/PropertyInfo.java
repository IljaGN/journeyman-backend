package ru.gvrn.journeyman.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.observers.api.Info;

@Getter
@RequiredArgsConstructor
public class PropertyInfo implements Info {
  public static final PropertyInfo EMPTY_INFO = new PropertyInfo("", new Object(), new Object());

  private final String name;
  private final Object oldValue;
  private final Object newValue;
}
