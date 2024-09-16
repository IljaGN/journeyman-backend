package ru.gvrn.journeyman.characters;

import ru.gvrn.journeyman.items.Item;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.outfits.api.Outfitter;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.IntegerRestrictedProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character implements Outfitter<OutfitItem> {
  protected final Map<String, Property<?>> characteristics;

  protected final Map<String, Item> items = new HashMap<>();
  protected final Body body;

  private IntegerRestrictedProperty carryingCapacity;
  private IntegerProperty armorClass;

  public Character(Map<String, Property<?>> characteristics, Body body) {
    this.characteristics = characteristics;
    this.body = body;
    carryingCapacity = (IntegerRestrictedProperty) characteristics.get("Carrying Capacity");
    armorClass = (IntegerProperty) characteristics.get("Armor Class");
  }

  @Override
  public List<OutfitItem> equip(List<OutfitItem> outfits) {
    outfits.forEach(it -> items.put(it.getUuid(), it));
    carryingCapacity.replaceValue(items.values().stream()
        .mapToInt(Item::getWeight).sum()
    );
    return body.equip(outfits);
  }

  @Override
  public List<OutfitItem> unequip(List<String> uuid) {
    return body.unequip(uuid);
  }
}
