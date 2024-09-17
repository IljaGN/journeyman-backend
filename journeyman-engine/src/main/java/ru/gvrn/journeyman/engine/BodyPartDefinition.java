package ru.gvrn.journeyman.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BodyPartDefinition {
  private final String name;
  private final String slot;
}
