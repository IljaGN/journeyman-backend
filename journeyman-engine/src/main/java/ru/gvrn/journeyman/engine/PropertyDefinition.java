package ru.gvrn.journeyman.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PropertyDefinition {
  private final String name;
  private final String type;
  private final String value;
}
