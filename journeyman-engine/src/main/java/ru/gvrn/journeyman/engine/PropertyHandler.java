package ru.gvrn.journeyman.engine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.dicees.BaseDicePool;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.values.CalculatedPropertyValue;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PropertyHandler {
  private static final Map<String, String> CH_MOD_MAP = new HashMap<>();

  static {
    CH_MOD_MAP.put("Strength", "STR");
    CH_MOD_MAP.put("Dexterity", "DEX");
    CH_MOD_MAP.put("Constitution", "CON");
    CH_MOD_MAP.put("Intelligence", "INT");
    CH_MOD_MAP.put("Wisdom", "WIS");
    CH_MOD_MAP.put("Charisma", "CHA");
  }

  private final PropertyDefinitionCsvConverter csvConverter;
  private final PropertyPropertyDefinitionConverter propertyConverter;

  public void handle() {
    propertyConverter.parse(csvConverter.getPropertyDefinitions());
    Map<String, PropertyAndValueDefinition<Integer>> nameIntegerPropertyMap = propertyConverter.getNameIntegerPropertyMap();
    CH_MOD_MAP.forEach((chName, modName) -> {
      PropertyValue<Integer> chValue = nameIntegerPropertyMap.get(chName).getValue();
      PropertyAndValueDefinition<Integer> modDefinition = nameIntegerPropertyMap.get(modName);
      PropertyValue<Integer> modValue = modDefinition.getValue();
      CalculatedPropertyValue<Integer> calcModValue = new CalculatedPropertyValue<>(modValue);
      modDefinition.setValue(calcModValue);
      calcModValue.observe(chValue);
      calcModValue.setDependenciesAndUpdate(info -> calculateModifier(chValue.getValue()));
    });
    PropertyValue<Integer> level = nameIntegerPropertyMap.get("Level").getValue();
    PropertyAndValueDefinition<Integer> totalHitPointsDef = nameIntegerPropertyMap.get("Total Hit Points");
    PropertyValue<Integer> totalHitPoints = totalHitPointsDef.getValue();
    CalculatedPropertyValue<Integer> calcTotalHitPoints = new CalculatedPropertyValue<>(totalHitPoints);
    totalHitPointsDef.setValue(calcTotalHitPoints);
    calcTotalHitPoints.observe(level);
    DicePool hpd = new BaseDicePool(level.getValue(), 8);
    calcTotalHitPoints.setDependenciesAndUpdate(info ->
        level.getValue() == 1 ? hpd.getMaxDiceEdge() : hpd.rollAndGetValue()
    );
  }

  public Map<String, Property<?>> getPropertyMap() {
    handle();
    return propertyConverter.getAllProperties();
  }

  private static int calculateModifier(int characteristic) {
    return characteristic / 2 - 5;
  }
}
