package ru.gvrn.journeyman.characters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.outfits.api.Equippable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class BodyPart implements Equippable<OutfitItem> {
  @Getter
  private final String name; // unique
  // TODO: переделать под слот, 1 часть 1 слот, нужно больше дроби на более мелки части например под кольца на пальцы
  private final Set<String> tags = new HashSet<>(); // не понятно нужна ли действительно если предмет знает все места для экипировки ?
  private OutfitItem item;

  @Override
  public boolean isFree() {
    return Objects.isNull(item);
  }

  @Override
  public OutfitItem equip(OutfitItem outfit) {
    return replace(outfit);
  }

  @Override
  public OutfitItem unequip() {
    return replace(null);
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  private OutfitItem replace(OutfitItem newOutfit) {
    OutfitItem oldOutfit = item;
    item = newOutfit;
    return oldOutfit;
  }
}
