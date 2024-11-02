package ru.gvrn.journeyman.items;

import ru.gvrn.journeyman.damages.DndDamage;
import ru.gvrn.journeyman.damages.api.Damage;
import ru.gvrn.journeyman.damages.api.Damaging;
import ru.gvrn.journeyman.dicees.BaseDicePool;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.List;
import java.util.Map;

// Предметы способные наносить урон, Оружие, Щит, бомбочки. Они получается могут быть как экипируемые так и нет
// Возможно нужна композиция на базе Item
public class DamagingItem extends Item implements Damaging { // и нужен интерфейс Damaging
  public DamagingItem(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
  }

  @Override
  public List<Damage> getDamage(String mode) {
    return List.of(DndDamage.builder()
        .type("")
        .damageDice(new BaseDicePool(6))
        .criticalRange(List.of(19, 20))
        .criticalMultiplier(2)
        .build());
  }
}
