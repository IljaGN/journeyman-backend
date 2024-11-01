package ru.gvrn.journeyman.items;

import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.List;
import java.util.Map;

// Предметы способные наносить урон, Оружие, Щит, бомбочки. Они получается могут быть как экипируемые так и нет
// Возможно нужна композиция на базе Item
public class DamagingItem extends Item { // и нужен интерфейс Damaging
  public DamagingItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
  }

  // Метод возвращающий урон
}
