package ru.gvrn.journeyman.outfits.api;

import java.util.List;

// Экипировка
public interface Outfit<T extends Outfit, E extends Equippable> {
  List<T> putOn(List<E> equippable);
}
