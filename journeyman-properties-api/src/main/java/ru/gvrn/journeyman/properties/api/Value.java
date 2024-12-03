package ru.gvrn.journeyman.properties.api;

//import ru.gvrn.journeyman.support.api.UniqueName;

public interface Value<T> { // extends UniqueName
  String getName(); // TODO: problem <!-- FIXME: Циклическая ссылка --> in 'journeyman-support-api'
  T getValue();
  void setValue(T value);
  Value<T> getInstance(String valueName); // Думаю нужно убрать из интерфейса
  void setOwnerName(String name);
}
