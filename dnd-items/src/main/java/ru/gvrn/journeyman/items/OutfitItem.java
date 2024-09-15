package ru.gvrn.journeyman.items;

import lombok.Setter;
import ru.gvrn.journeyman.outfits.api.Equippable;
import ru.gvrn.journeyman.outfits.api.Outfit;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// Это Armor и другие надеваемые предметы, кольцо, кулон
public class OutfitItem extends Item implements Outfit<OutfitItem> {
  // Тут тоже должно быть что-то типо патерна State, так как в зависимости от размера предмета по сравнению с размером персонажа
  // Он может быть двуручным/одноручным или терять возможность быть экипированным

  //    protected Map<String, BaseProperty> ownerCharacteristics; // final

  @Setter
  private List<String> slotsNames;

  public OutfitItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
  }

  @Override
  public List<OutfitItem> putOn(List<? extends Equippable<OutfitItem>> acceptors) {
    List<? extends Equippable<OutfitItem>> suitableAcceptors = acceptors.stream()
        .filter(bp -> slotsNames.contains(bp.getName()))
        .collect(Collectors.toList());

    if (suitableAcceptors.size() >= slotsNames.size()) { //TODO: можно занять больше слотов чем требуется
      return suitableAcceptors.stream()
          .map(bp -> bp.equip(this))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }

    return Collections.singletonList(this);
  }
}
