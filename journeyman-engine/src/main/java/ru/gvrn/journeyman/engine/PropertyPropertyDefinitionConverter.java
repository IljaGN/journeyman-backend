package ru.gvrn.journeyman.engine;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class PropertyPropertyDefinitionConverter {
  private static final String PROPERTY_TYPE_SPECIAL = "0";
  private static final String PROPERTY_TYPE_STRING = "1";
  private static final String PROPERTY_TYPE_INTEGER = "2";
  private static final String PROPERTY_TYPE_BOOLEAN = "3";

  private final Map<String, PropertyAndValueDefinition<Boolean>> nameBooleanPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<DicePool>> nameDicePoolPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<Integer>> nameIntegerPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<String>> nameStringPropertyMap = new HashMap<>();

  public void parse(List<PropertyDefinition> definitions) {
    for (PropertyDefinition definition : definitions) {
      switch (definition.getType()) {
        case PROPERTY_TYPE_SPECIAL:
          break;
        case PROPERTY_TYPE_STRING: {
          PropertyValue<String> value = new PropertyValue<>("current", definition.getValue());
          PropertyAndValueDefinition<String> pvDefinition = new PropertyAndValueDefinition<>(definition.getName(), StringProperty.class);
          pvDefinition.setValue(value);
          nameStringPropertyMap.put(definition.getName(), pvDefinition);
        }
        break;
        case PROPERTY_TYPE_INTEGER: {
          PropertyValue<Integer> value = new PropertyValue<>("current", Integer.valueOf(definition.getValue()));
          PropertyAndValueDefinition<Integer> pvDefinition = new PropertyAndValueDefinition<>(definition.getName(), IntegerProperty.class);
          pvDefinition.setValue(value);
          nameIntegerPropertyMap.put(definition.getName(), pvDefinition);
        }
        break;
        case PROPERTY_TYPE_BOOLEAN: {
          PropertyValue<Boolean> value = new PropertyValue<>("current", Boolean.valueOf(definition.getValue()));
          PropertyAndValueDefinition<Boolean> pvDefinition = new PropertyAndValueDefinition<>(definition.getName(), BooleanProperty.class);
          pvDefinition.setValue(value);
          nameBooleanPropertyMap.put(definition.getName(), pvDefinition);
        }
        break;
        default:
          // TODO: error
          break;
      }
    }
  }
}
