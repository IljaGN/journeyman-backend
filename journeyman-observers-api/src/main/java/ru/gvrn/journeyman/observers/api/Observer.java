package ru.gvrn.journeyman.observers.api;

public interface Observer {
  void update(Info info);

  default void observe(Observable observable) {
    observable.add(this);
  }
}
