package ru.gvrn.journeyman.items;

import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

public class DamagingOutfitItem extends OutfitItem { // implements Damaging
  private DamagingItem damaging;

  public DamagingOutfitItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
    damaging = new DamagingItem(id, properties);
  }
}
