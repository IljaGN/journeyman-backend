package ru.gvrn.journeyman.observers.api;

public interface Observable {
  void add(Observer observer);
  void delete(Observer observer);
  void deleteObservers();
  void notify(Info info);
}
