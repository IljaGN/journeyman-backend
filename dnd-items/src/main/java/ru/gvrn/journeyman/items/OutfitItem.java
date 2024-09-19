package ru.gvrn.journeyman.items;

import lombok.Setter;
import ru.gvrn.journeyman.outfits.api.Equippable;
import ru.gvrn.journeyman.outfits.api.Outfit;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// Это Armor и другие надеваемые предметы, кольцо, кулон
public class OutfitItem extends Item implements Outfit<OutfitItem> {
  // Тут тоже должно быть что-то типо патерна State, так как в зависимости от размера предмета по сравнению с размером персонажа
  // Он может быть двуручным/одноручным или терять возможность быть экипированным

  //    protected Map<String, BaseProperty> ownerCharacteristics; // final
  private final IntegerProperty armor;
  private final IntegerProperty maxDexBonus;

  @Setter
  private List<String> slotIds;

  public OutfitItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
    this.armor = (IntegerProperty) properties.get("Armor");
    this.maxDexBonus = (IntegerProperty) properties.get("Max Dex Bonus");
  }

  @Override
  public List<OutfitItem> putOn(List<? extends Equippable<OutfitItem>> acceptors) {
    List<? extends Equippable<OutfitItem>> suitableAcceptors = acceptors.stream()
        .filter(acceptor -> slotIds.contains(acceptor.getSlotId()))
        .collect(Collectors.toList());

    if (suitableAcceptors.size() >= slotIds.size() && !slotIds.isEmpty()) {
      if (suitableAcceptors.size() == slotIds.size()) {
        return suitableAcceptors.stream()
            .map(acceptor -> acceptor.equip(this))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
      }

      ArrayList<OutfitItem> result = new ArrayList<>();
      for (String slotId : slotIds) {
        Equippable<OutfitItem> firstAcc = suitableAcceptors.stream()
            .filter(acceptor -> slotId.equals(acceptor.getSlotId()))
            .findFirst().get();
        suitableAcceptors.remove(firstAcc);
        OutfitItem unequip = firstAcc.equip(this);
        if (Objects.nonNull(unequip)) {
          result.add(unequip);
        }
      }
      return result;
    }

    return List.of(this);
  }

  public Integer getArmor() {
    return armor.getValue();
  }

  public Integer getMaxDexBonus() {
    return maxDexBonus.getValue();
  }
}
