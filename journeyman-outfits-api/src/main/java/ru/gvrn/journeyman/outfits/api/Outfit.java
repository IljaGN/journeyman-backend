package ru.gvrn.journeyman.outfits.api;

import java.util.List;

// Экипировка
public interface Outfit<T extends Outfit<?>> {
  List<T> putOn(List<? extends Equippable<T>> equippable);
}
