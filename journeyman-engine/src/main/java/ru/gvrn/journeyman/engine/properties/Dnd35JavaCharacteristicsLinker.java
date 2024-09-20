package ru.gvrn.journeyman.engine.properties;

import lombok.Builder;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.dicees.BaseDicePool;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.engine.properties.models.PropertyAndValueDefinition;
import ru.gvrn.journeyman.observers.api.Observable;
import ru.gvrn.journeyman.properties.values.CalculatedPropertyValue;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.HashMap;
import java.util.Map;

@Component
public class Dnd35JavaCharacteristicsLinker {
  private static final Map<String, String> CH_MOD_MAP = new HashMap<>();
  static {
    CH_MOD_MAP.put("Strength", "STR");
    CH_MOD_MAP.put("Dexterity", "DEX");
    CH_MOD_MAP.put("Constitution", "CON");
    CH_MOD_MAP.put("Intelligence", "INT");
    CH_MOD_MAP.put("Wisdom", "WIS");
    CH_MOD_MAP.put("Charisma", "CHA");
  }

  public void bind(LinkContainer container) {
    Map<String, PropertyAndValueDefinition<Integer>> nameIntegerDefinitionMap = container.nameIntegerDefinitionMap;
    CH_MOD_MAP.forEach((chName, modName) -> {
      PropertyValue<Integer> chValue = nameIntegerDefinitionMap.get(chName).getValue();
      PropertyAndValueDefinition<Integer> modDefinition = nameIntegerDefinitionMap.get(modName);
      CalculatedPropertyValue<Integer> calcModValue = makeObservant(modDefinition, chValue);
      calcModValue.setDependenciesAndUpdate(info -> calculateModifier(chValue.getValue()));
    });
    PropertyValue<Integer> level = nameIntegerDefinitionMap.get("Level").getValue();
    PropertyAndValueDefinition<Integer> totalHitPointsDef = nameIntegerDefinitionMap.get("Total Hit Points");
    CalculatedPropertyValue<Integer> calcTotalHitPoints = makeObservant(totalHitPointsDef, level);
    DicePool hpd = new BaseDicePool(level.getValue(), 8); // Hit Point Dice
    calcTotalHitPoints.setDependenciesAndUpdate(info ->
        level.getValue() == 1 ? hpd.getMaxDiceEdge() : calcTotalHitPoints.getValue() + hpd.rollAndGetValue()
    );
  }

  private <T> CalculatedPropertyValue<T> makeObservant(PropertyAndValueDefinition<T> observantDefinition,
                                                       Observable observable) {
    CalculatedPropertyValue<T> observant = new CalculatedPropertyValue<>(observantDefinition.getValue());
    observantDefinition.setValue(observant);
    observant.observe(observable);
    return observant;
  }

  private static int calculateModifier(int characteristic) {
    return characteristic / 2 - 5;
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
