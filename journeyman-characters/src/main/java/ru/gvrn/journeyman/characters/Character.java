package ru.gvrn.journeyman.characters;

import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

public class Character {
  protected final Map<String, Property<?>> characteristics;

//  protected final Map<String, Item> items = new HashMap<>();
//  protected final Body body;

  public Character(Map<String, Property<?>> characteristics) {
    this.characteristics = characteristics;
  }
}
