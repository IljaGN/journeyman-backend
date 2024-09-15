package ru.gvrn.journeyman.outfits.api;

import java.util.List;

// Тот кто имеет возможность экипироваться
public interface Outfitter<T extends Outfit<?>> {
  List<T> equip(List<T> outfits);
  List<T> unequip(List<String> ids); //TODO: привязываемся к конкретному типу id!
}
