package ru.gvrn.journeyman.items;

import lombok.Setter;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.List;
import java.util.Map;

// Предметы способные наносить урон, Оружие, Щит, бомбочки. Они получается могут быть как экипируемые так и нет
// Возможно нужна композиция на базе Item
public class Damaging extends Item {
  public Damaging(Long id, Map<String, Property<?>> properties) {
    super(id, properties);
  }

  @Setter
  private List<String> slotsNames;

  // TODO: это должно быть в каком-то своем модуле но я пока не понял в каком
  public interface Damage {
    boolean isCritical(int naturalD20Value);
    void cause(Map<String, Property<?>> characteristics);
    void cause(Map<String, Property<?>> characteristics, int times);
    void causeCritical(Map<String, Property<?>> characteristics);
    void causeCritical(Map<String, Property<?>> characteristics, int times);
  }

  public static class DamageImpl implements Damage {
    public static final String CURRENT_HIT_POINTS_PROPERTY_NAME = "Current Hit Points";

    private String type;
    private DicePool damageDice;
    private List<Integer> criticalRange;
    private int criticalMultiplier;

    @Override
    public boolean isCritical(int naturalD20Value) {
      return criticalRange.contains(naturalD20Value);
    }

    @Override
    public void cause(Map<String, Property<?>> characteristics) {
      cause(characteristics, 1);
    }

    @Override
    public void cause(Map<String, Property<?>> characteristics, int times) {
      makeDamage(characteristics, times);
    }

    @Override
    public void causeCritical(Map<String, Property<?>> characteristics) {
      causeCritical(characteristics, 1);
    }

    @Override
    public void causeCritical(Map<String, Property<?>> characteristics, int times) {
      makeDamage(characteristics, times * criticalMultiplier);
    }

    protected void makeDamage(Map<String, Property<?>> characteristics, int multiplier) {
      Property<?> currentHitPoints = characteristics.get(CURRENT_HIT_POINTS_PROPERTY_NAME);
      Property<?> dmgResist = characteristics.get(type); // through additional table map.get(type)
      int dmg = 0;
      for (int i = 0; i < multiplier; i++) {
        dmg += damageDice.rollAndGetValue();
      }
      // тут как-то нужно модифицировать урон через dmgResist если он не null
//      AddingVisitor dmgVisitor = new AddingVisitor(-normalizeDamage(dmg)); // Тут нужна фабрика, так как в общем случае не знаем тип и что делать
//      currentHitPoints.accept(dmgVisitor);
    }

    protected int normalizeDamage(int damage) {
      return damage > 0 ? damage : 1;
    }
  }
}
