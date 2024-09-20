package ru.gvrn.journeyman.characters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.outfits.api.Equippable;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BodyPart implements Equippable<OutfitItem> {
  @Getter
  private final String name; // unique
  @Getter
  private final String slotId;
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

  public OutfitItem unequip(List<String> uuid) {
    return uuid.contains(item.getUuid()) ? unequip() : null;
  }

  public Integer getArmor() {
    return Objects.isNull(item) ? 0 : item.getArmor();
  }

  public Integer getMaxDexBonus() {
    return Objects.isNull(item) ? Integer.MAX_VALUE : item.getMaxDexBonus();
  }

  private OutfitItem replace(OutfitItem newOutfit) {
    OutfitItem oldOutfit = item;
    item = newOutfit;
    return oldOutfit;
  }
}
