package ru.gvrn.journeyman.attacks.api;

import ru.gvrn.journeyman.support.api.Characteristics;

public interface Attack {
  void assault(Characteristics attacked);
}
