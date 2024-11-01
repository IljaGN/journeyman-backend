package ru.gvrn.journeyman.damages.api;

import java.util.List;

public interface Damaging {
  List<Damage> getDamage(String mode);
}
