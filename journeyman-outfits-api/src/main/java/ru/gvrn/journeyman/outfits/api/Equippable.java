package ru.gvrn.journeyman.outfits.api;

// То что экипируют экипировкой
public interface Equippable<T extends Outfit<?>> {
  String getName();
  String getSlotId();
  boolean isFree();
  T equip(T outfit); // return T if replace Outfit, else null
  T unequip(); // return T if have Outfit, else null
}
