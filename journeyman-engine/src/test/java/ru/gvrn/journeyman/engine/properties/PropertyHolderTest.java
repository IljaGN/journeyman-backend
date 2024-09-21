package ru.gvrn.journeyman.engine.properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.engine.EngineApp;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.gvrn.journeyman.engine.constants.Dnd35ChPropertyNames.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
class PropertyHolderTest {
  @Autowired
  private PropertyHolder propertyHolder;

  @BeforeEach
  void beforeTest() {
    propertyHolder.handle(new Body());
  }

  @Test
  public void linkProperties_isCorrect() {
    Map<String, Object> expNameValueMap = getModValuesMap();
    Map<String, Object> actNameValueMap = getChValuesMap();
    Map<String, Property> namePropertyMap = propertyHolder.getPropertyMap().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    actNameValueMap.forEach((name, value) -> namePropertyMap.get(name).replaceValue(value));
    expNameValueMap.forEach((name, value) -> {
      switch (name) {
        case TOTAL_HIT_POINTS:
          Integer exp = (Integer) value;
          Property hitProperty = namePropertyMap.get(name);
          hitProperty.resetOnDefault();
          Integer hit = (Integer) hitProperty.getValue();
          assertTrue(exp < hit);
          break;
        case CARRYING_CAPACITY:
          Property capProperty = namePropertyMap.get(name);
          assertEquals(0, capProperty.getValue());
          capProperty.resetOnDefault();
          assertEquals(value, capProperty.getValue());
          break;
        default:
          assertEquals(value, namePropertyMap.get(name).getValue());
      }
    });
  }

  private Map<String, Object> getModValuesMap() {
    Map<String, Object> map = new HashMap<>();
    map.put(STR, -2);
    map.put(DEX, -1);
    map.put(CON, 0);
    map.put(INT, 1);
    map.put(WIS, 2);
    map.put(CHA, 3);
    map.put(FORTITUDE, 2); // CON + X_BONUS
    map.put(REFLEX, 0); // DEX + X_BONUS
    map.put(WILL, 3); // WIS + X_BONUS
    map.put(INITIATIVE_MOD, -1); // DEX
    map.put(ARMOR_CLASS, 9); // 10 + DEX
    map.put(CARRYING_CAPACITY, 36000); // STRENGTH * 6 - 6
    map.put(TOTAL_HIT_POINTS, 8);
    return map;
  }

  private Map<String, Object> getChValuesMap() {
    Map<String, Object> map = new HashMap<>();
    map.put(STRENGTH, 7);
    map.put(DEXTERITY, 9);
    map.put(CONSTITUTION, 10);
    map.put(INTELLIGENCE, 13);
    map.put(WISDOM, 14);
    map.put(CHARISMA, 16);
    map.put(FORTITUDE_BONUS, 2);
    map.put(REFLEX_BONUS, 1);
    map.put(WILL_BONUS, 1);
    map.put(LEVEL, 2);
    return map;
  }
}
