package ru.gvrn.journeyman.characters;

import lombok.Getter;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.observers.SupportObservable;
import ru.gvrn.journeyman.outfits.api.Outfitter;
import ru.gvrn.journeyman.properties.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Body extends SupportObservable implements Outfitter<OutfitItem> {
  private final List<BodyPart> parts = new ArrayList<>(); // Возможно нужен класс BodyParts в котором и лежит List<BodyPart>

  // Как будто завязка на конкретные правила
  @Getter
  private int maxDexBonus = Integer.MAX_VALUE;
  @Getter
  private int armorClass = 0; // getNonFreeBodyPartStream() тут надо подумать возможно сама часть тела может иметь некий показатель
  // Но в целом думаю это не нужно можно заложить повышенное сопротивление в базовый бонус брони и не усложнять
  // end

  public void add(BodyPart part) {
    parts.add(part);
  }

  public BodyPart remove(String name) {
    return parts.stream()
        .filter(bp -> bp.getName().equals(name))
        .findFirst().orElseThrow(); // TODO: exception
  }

  @Override
  public List<OutfitItem> equip(List<OutfitItem> items) {
    List<OutfitItem> unequipItems = items.stream()
        .flatMap(it -> it.putOn(parts).stream())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    updateParametersAndNotify();
    return unequipItems;
  }

  @Override
  public List<OutfitItem> unequip(List<String> uuid) {
    List<OutfitItem> unequipItems = getNonFreeBodyPartStream()
        .map(bp -> bp.unequip(uuid))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    updateParametersAndNotify();
    return unequipItems;
  }

  // TODO: еще лучше делать расчет прямо в гетерах. У самой вещи могут измениться значения показателя
  protected void updateParametersAndNotify() {
    int totalArmor = getNonFreeBodyPartStream().mapToInt(BodyPart::getArmor).sum();
    int dexLimit = getNonFreeBodyPartStream().mapToInt(BodyPart::getMaxDexBonus)
        .reduce(Integer.MAX_VALUE, Integer::min);
    if (armorClass != totalArmor) {
      int oldArmorClass = armorClass;
      armorClass = totalArmor;
      notify(new PropertyInfo(getClass().getSimpleName(), oldArmorClass, armorClass));
    }
    if (maxDexBonus != dexLimit) {
      int oldMaxDexBonus = maxDexBonus;
      maxDexBonus = dexLimit;
      notify(new PropertyInfo(getClass().getSimpleName(), oldMaxDexBonus, maxDexBonus));
    }
  }

  protected Stream<BodyPart> getNonFreeBodyPartStream() {
    return parts.stream().filter(bp -> !bp.isFree());
  }
}
