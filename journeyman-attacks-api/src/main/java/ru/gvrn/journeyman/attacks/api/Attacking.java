package ru.gvrn.journeyman.attacks.api;

public interface Attacking {
  Attack getAttack(String damagingUuid, String mode);
}
