package ru.gvrn.journeyman.damages;

import lombok.Builder;
import ru.gvrn.journeyman.damages.api.Damage;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.List;
import java.util.Map;

@Builder
public class DndDamage implements Damage {
  public static final String CURRENT_HIT_POINTS_PROPERTY_NAME = "Current Hit Points";

  private final String type;
  private final DicePool damageDice;
  private final List<Integer> criticalRange;
  private final int criticalMultiplier;

  @Override
  public boolean isCritical(int naturalD20Value) {
    return criticalRange.contains(naturalD20Value);
  }

  @Override
  public void cause(Map<String, Property<?>> characteristics, int times) {
    makeDamage(characteristics, times);
  }

  @Override
  public void causeCritical(Map<String, Property<?>> characteristics, int times) {
    makeDamage(characteristics, times*criticalMultiplier);
  }

  protected void makeDamage(Map<String, Property<?>> characteristics, int multiplier) {
    Property<?> currentHitPoints = characteristics.get(CURRENT_HIT_POINTS_PROPERTY_NAME);
    Property<?> dmgResist = characteristics.get(type); // through additional table map.get(type)
    int dmg = 0;
    for (int i = 0; i < multiplier; i++) {
      dmg += damageDice.rollAndGetValue();
    }
    // тут как-то нужно модифицировать урон через dmgResist если он не null
//    AddingVisitor dmgVisitor = new AddingVisitor(-normalizeDamage(dmg)); // Тут нужна фабрика, так как в общем случае не знаем тип и что делать
//    currentHitPoints.accept(dmgVisitor);
  }

  protected int normalizeDamage(int damage) {
    return damage > 0 ? damage : 1;
  }
}
