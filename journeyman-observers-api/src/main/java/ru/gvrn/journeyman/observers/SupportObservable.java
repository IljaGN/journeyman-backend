package ru.gvrn.journeyman.observers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observable;
import ru.gvrn.journeyman.observers.api.Observer;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class SupportObservable implements Observable {
  private final Collection<Observer> observers;

  public SupportObservable() {
    this.observers = new ArrayList<>();
  }

  @Override
  public void add(@NonNull Observer observer) {
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
  public void notify(@NonNull Info info) {
    observers.forEach(obs -> obs.update(info));
  }
}
