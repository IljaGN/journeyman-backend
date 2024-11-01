package ru.gvrn.journeyman.engine.attack;

public interface Attacking {
  Attack getAttack(String weaponUuid, String mode);
}
