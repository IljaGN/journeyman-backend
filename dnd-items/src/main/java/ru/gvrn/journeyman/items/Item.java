package ru.gvrn.journeyman.items;

import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

// Просто предметы, монеты они вроде не экипируются и просто лежат в инвентаре, у них два свойства цена и вес
@RequiredArgsConstructor
public class Item {
  protected final Long id;
  protected final Map<String, Property<?>> properties;
//  protected final IntegerProperty weight;
// int size; // возможно достаточно поля в properties
// State state; // возможно достаточно поля в properties
}
