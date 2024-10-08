package ru.gvrn.journeyman.engine.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.dicees.api.DicePool;
import ru.gvrn.journeyman.engine.properties.models.PropertyAndValueDefinition;
import ru.gvrn.journeyman.engine.properties.models.PropertyDefinition;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.IntegerRestrictedProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
@Component
@RequiredArgsConstructor
public class PropertyValueDefinitionSource {
  private static final String PROPERTY_TYPE_SPECIAL = "0";
  private static final String PROPERTY_TYPE_STRING = "1";
  private static final String PROPERTY_TYPE_INTEGER = "2";
  private static final String PROPERTY_TYPE_BOOLEAN = "3";

  private final Map<String, PropertyAndValueDefinition<Boolean>> nameBooleanPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<DicePool>> nameDicePoolPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<Integer>> nameIntegerPropertyMap = new HashMap<>();
  private final Map<String, PropertyAndValueDefinition<String>> nameStringPropertyMap = new HashMap<>();

  @Getter(AccessLevel.NONE)
  private final PropertyDefinitionSource definitionSource;

  public void parse() {
    parse(definitionSource.getPropertyDefinitions());
  }

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

  public Map<String, Property<?>> getAllProperties() {
    Map<String, Property<?>> map = new HashMap<>(convert(nameBooleanPropertyMap));
    map.putAll(convert(nameDicePoolPropertyMap));
    map.putAll(convert(nameIntegerPropertyMap));
    map.putAll(convert(nameStringPropertyMap));
    return map;
  }

  public Map<String, Property<?>> convert(Map<String, ? extends PropertyAndValueDefinition<?>> map) {
    final Map<String, Property<?>> result = new HashMap<>();
    map.forEach((name, def) -> {
      Class<?> propertyClass = def.getType();
      PropertyValue<?> current = def.getValue();
      Collection<? extends PropertyValue<?>> anotherValues = def.getValues();
      anotherValues.remove(current);
      Constructor<?>[] constructors = propertyClass.getDeclaredConstructors();
      int constructorIndex = anotherValues.size();
      if (propertyClass.equals(IntegerProperty.class) && anotherValues.size() > 1) {
        constructors = IntegerRestrictedProperty.class.getDeclaredConstructors();
        constructorIndex = 0;
      }
      Property<?> property = createProperty(constructors, name, current, anotherValues, constructorIndex);
      result.put(name, property);
    });
    return result;
  }

  @SneakyThrows
  private Property<?> createProperty(Constructor<?>[] constructors, String name, PropertyValue<?> current,
                                     Collection<? extends PropertyValue<?>> anotherValues, int ci) {
    Object[] args = new Object[anotherValues.size() + 2];
    args[0] = name;
    args[1] = current;
    Iterator<? extends PropertyValue<?>> iterator = anotherValues.iterator();
    for (int i = 2; iterator.hasNext(); i++) {
      args[i] = iterator.next();
    }
    return (Property<?>) constructors[ci].newInstance(args); // TODO:not save
  }
}
