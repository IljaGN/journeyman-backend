package ru.gvrn.journeyman.items;

import lombok.Setter;
import ru.gvrn.journeyman.outfits.api.Equippable;
import ru.gvrn.journeyman.outfits.api.Outfit;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.List;
import java.util.Map;

// Это Armor и другие надеваемые предметы, кольцо, кулон
public class OutfitItem extends Item implements Outfit<OutfitItem, Equippable> { //Equippable -> BodyPart
  @Setter
  private List<String> slotsNames;

  public OutfitItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
  }

  @Override
  public List<OutfitItem> putOn(List<Equippable> equippable) { //Equippable -> BodyPart
    return null;
  }
}
