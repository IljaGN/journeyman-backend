package ru.gvrn.journeyman.engine.properties;

import lombok.Builder;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.dicees.ConstantDicePool;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.engine.properties.models.PropertyAndValueDefinition;
import ru.gvrn.journeyman.observers.api.Observable;
import ru.gvrn.journeyman.properties.values.CalculatedPropertyValue;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.gvrn.journeyman.engine.constants.Dnd35ChPropertyNames.*;

@Component
public class Dnd35JavaCharacteristicsLinker {
  private static final String SAVING_THROW_BONUS = " Save Bonus";

  private static final Map<String, String> CH_MOD_MAP = new HashMap<>();
  private static final Map<String, String> SAVING_THROWS_MOD_MAP = new HashMap<>();
  static {
    CH_MOD_MAP.put(STRENGTH, STR);
    CH_MOD_MAP.put(DEXTERITY, DEX);
    CH_MOD_MAP.put(CONSTITUTION, CON);
    CH_MOD_MAP.put(INTELLIGENCE, INT);
    CH_MOD_MAP.put(WISDOM, WIS);
    CH_MOD_MAP.put(CHARISMA, CHA);

    SAVING_THROWS_MOD_MAP.put(FORTITUDE, CON);
    SAVING_THROWS_MOD_MAP.put(REFLEX, DEX);
    SAVING_THROWS_MOD_MAP.put(WILL, WIS);
  }

  public void bind(LinkContainer container) {
    Map<String, PropertyAndValueDefinition<Integer>> nameIntegerDefinitionMap = container.nameIntegerDefinitionMap;
    Body body = container.body;
    CH_MOD_MAP.forEach((chName, modName) -> {
      PropertyValue<Integer> chValue = nameIntegerDefinitionMap.get(chName).getValue();
      PropertyAndValueDefinition<Integer> modDefinition = nameIntegerDefinitionMap.get(modName);
      CalculatedPropertyValue<Integer> calcModValue = makeObservant(modDefinition, chValue);
      calcModValue.setDependenciesAndUpdate(info -> calculateModifier(chValue.getValue()));
    });

    SAVING_THROWS_MOD_MAP.forEach((stName, modName) -> {
      PropertyValue<Integer> modValue = nameIntegerDefinitionMap.get(modName).getValue();
      PropertyValue<Integer> stBonus = nameIntegerDefinitionMap.get(stName + SAVING_THROW_BONUS).getValue();
      PropertyAndValueDefinition<Integer> stDefinition = nameIntegerDefinitionMap.get(stName);
      CalculatedPropertyValue<Integer> calcStValue = makeObservant(stDefinition, modValue, stBonus);
      calcStValue.setDependenciesAndUpdate(info -> stBonus.getValue() + modValue.getValue());
    });

    PropertyValue<Integer> dexMod = nameIntegerDefinitionMap.get(DEX).getValue();
    PropertyAndValueDefinition<Integer> initModDefinition = nameIntegerDefinitionMap.get(INITIATIVE_MOD);
    CalculatedPropertyValue<Integer> calcInitMod = makeObservant(initModDefinition, dexMod);
    calcInitMod.setDependenciesAndUpdate(info -> dexMod.getValue());

    PropertyAndValueDefinition<Integer> acDefinition = nameIntegerDefinitionMap.get(ARMOR_CLASS);
    CalculatedPropertyValue<Integer> calcAc = makeObservant(acDefinition, dexMod, body); // TODO: зависит от size
    calcAc.setDependenciesAndUpdate(info -> {
      // логику ниже с dexBonus спрятать, а формулу можно и переписать
      int dexBonus = Math.min(dexMod.getValue(), body.getMaxDexBonus());
      return 10 + body.getArmorClass() + dexBonus; // + size нужна таблица
    });

    PropertyValue<Integer> strength = nameIntegerDefinitionMap.get(STRENGTH).getValue();
    PropertyAndValueDefinition<Integer> capacityDefinition = nameIntegerDefinitionMap.get(CARRYING_CAPACITY);
    PropertyValue<Integer> capCurrent = capacityDefinition.getValue();
    CalculatedPropertyValue<Integer> capMax = new CalculatedPropertyValue<>("max", capCurrent.getValue());
    PropertyValue<Integer> capMin = new PropertyValue<>("min", capCurrent.getValue());
    capMax.observe(strength);
    capMax.setDependenciesAndUpdate(info -> (strength.getValue()*6 - 6)*1000); // TODO: примерно, нужна таблица или более точная формула
    capacityDefinition.setValue(capMax);
    capacityDefinition.setValue(capCurrent);
    capacityDefinition.setValue(capMin);

    PropertyValue<Integer> level = nameIntegerDefinitionMap.get(LEVEL).getValue();
    PropertyValue<Integer> conMod = nameIntegerDefinitionMap.get(CON).getValue();
    PropertyAndValueDefinition<Integer> hitDefinition = nameIntegerDefinitionMap.get(TOTAL_HIT_POINTS);
    PropertyValue<Integer> hitCurrent = hitDefinition.getValue();
    CalculatedPropertyValue<Integer> hitMax = new CalculatedPropertyValue<>("max", hitCurrent.getValue());
    PropertyValue<Integer> hitMin = new PropertyValue<>("min", hitCurrent.getValue());
    hitMax.observe(level);
    hitMax.observe(conMod); // там все сложнее поэтому пока простой вариант
//    DicePool hpd = new BaseDicePool(8); // Hit Point Dice, определяется CHARACTER_CLASS
    DicePool hpd = new ConstantDicePool(8, 5); // TODO: only for CharacterTest
    hitMax.setDependenciesAndUpdate(info -> { // данная схема работает если уровень увеличивается на 1 за раз, что в целом справедливо
      int rollValue = level.getValue() == 1 ? hpd.getMaxDiceEdge() : hpd.rollAndGetValue();
      int additionalHp = Math.max(rollValue + conMod.getValue(), 1);
      return hitMax.getValue() + additionalHp;
    });
    hitCurrent.setValue(hitMax.getValue());
    hitDefinition.setValue(hitMax);
    hitDefinition.setValue(hitCurrent);
    hitDefinition.setValue(hitMin);
  }

  private <T> CalculatedPropertyValue<T> makeObservant(PropertyAndValueDefinition<T> observantDefinition,
                                                       Observable... observables) {
    CalculatedPropertyValue<T> observant = new CalculatedPropertyValue<>(observantDefinition.getValue());
    observantDefinition.setValue(observant);
    Arrays.stream(observables).forEach(observant::observe);
    return observant;
  }

  private static int calculateModifier(int characteristic) {
    return characteristic/2 - 5;
  }

  @Builder
  public static class LinkContainer {
    private final Map<String, PropertyAndValueDefinition<Boolean>> nameBooleanDefinitionMap;
    private final Map<String, PropertyAndValueDefinition<DicePool>> nameDicePoolDefinitionMap;
    private final Map<String, PropertyAndValueDefinition<Integer>> nameIntegerDefinitionMap;
    private final Map<String, PropertyAndValueDefinition<String>> nameStringDefinitionMap;
    private final Body body;
  }
}
