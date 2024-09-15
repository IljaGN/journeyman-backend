package ru.gvrn.journeyman.observers.api;

public interface Info {
  String getIdentifier();
  Object getOldValue();
  Object getNewValue();

  default String getDisplayedIdentifier() {
    return getIdentifier();
  }
}
