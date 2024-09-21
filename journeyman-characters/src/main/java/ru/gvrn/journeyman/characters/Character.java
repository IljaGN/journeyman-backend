package ru.gvrn.journeyman.characters;

import ru.gvrn.journeyman.items.Item;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.outfits.api.Outfitter;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.IntegerRestrictedProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Character implements Outfitter<OutfitItem> {
  protected final Map<String, Property<?>> characteristics;

  protected final Map<String, Item> uuidItemMap = new HashMap<>();
  protected final Body body;

  private IntegerRestrictedProperty carryingCapacity;
  private IntegerProperty armorClass;

  public Character(Map<String, Property<?>> characteristics, Body body) {
    this.characteristics = new HashMap<>(characteristics);
    this.body = body;
    carryingCapacity = (IntegerRestrictedProperty) characteristics.get("Carrying Capacity");
    armorClass = (IntegerProperty) characteristics.get("Armor Class");
  }

  public List<Item> equipFormInventory(List<String> uuids) {
    Map<Boolean, List<Item>> trueFalseOutfitItemMap = uuids.stream()
        .map(uuidItemMap::get)
        .filter(Objects::nonNull)
        .collect(Collectors.partitioningBy(item -> item instanceof OutfitItem));

    List<Item> items = trueFalseOutfitItemMap.get(Boolean.FALSE);
    List<OutfitItem> outfitItems = trueFalseOutfitItemMap.get(Boolean.TRUE).stream()
        .map(item -> (OutfitItem) item)
        .collect(Collectors.toList());
    items.addAll(body.equip(outfitItems));
    return items;
  }

  @Override
  public List<OutfitItem> equip(List<OutfitItem> outfits) {
    addInInventory(outfits);
    return body.equip(outfits);
  }

  @Override
  public List<OutfitItem> unequip(List<String> uuids) {
    return body.unequip(uuids);
  }

  public void addInInventory(Collection<? extends Item> items) {
    items.forEach(item -> uuidItemMap.put(item.getUuid(), item));
    updateCarryingCapacity();
  }

  public List<Item> removeFormInventory(Collection<String> uuids) {
    List<Item> removeItems = uuids.stream()
        .map(uuidItemMap::remove)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    updateCarryingCapacity();
    return removeItems;
  }

  protected void updateCarryingCapacity() {
    // TODO: это свойство так же должно следить за весом самих предметов в инвентаре, так как вес предмета
    //  может измениться. А перекладки нет carryingCapacity.value += newItemW - oldItemW
    carryingCapacity.replaceValue(uuidItemMap.values().stream()
        .mapToInt(Item::getWeight).sum()
    );
  }
}
