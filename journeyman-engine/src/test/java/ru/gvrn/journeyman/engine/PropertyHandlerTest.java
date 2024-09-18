package ru.gvrn.journeyman.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@TestPropertySource(properties = "dnd_properties.csv")
@ContextConfiguration(classes = EngineApp.class)
class PropertyHandlerTest {

  @Autowired
  private PropertyHandler propertyHandler;

  @Test
  public void linkProperties_isCorrect() {
    Map<String, Object> expNameValueMap = getModValuesMap();
    Map<String, Object> actNameValueMap = getChValuesMap();
    Map<String, Property> namePropertyMap = propertyHandler.getPropertyMap().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    actNameValueMap.forEach((name, value) -> namePropertyMap.get(name).replaceValue(value));
    expNameValueMap.forEach((name, value) -> {
      if ("Total Hit Points".equals(name)) {
        Integer exp = (Integer) value;
        Integer hit = (Integer) namePropertyMap.get(name).getValue();
        assertTrue(exp < hit);
      } else {
        assertEquals(value, namePropertyMap.get(name).getValue());
      }
    });
  }

  private Map<String, Object> getModValuesMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("STR", -2);
    map.put("DEX", -1);
    map.put("CON", 0);
    map.put("INT", 1);
    map.put("WIS", 2);
    map.put("CHA", 3);
    map.put("Total Hit Points", 8);
    return map;
  }

  private Map<String, Object> getChValuesMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("Strength", 7);
    map.put("Dexterity", 9);
    map.put("Constitution", 10);
    map.put("Intelligence", 13);
    map.put("Wisdom", 14);
    map.put("Charisma", 16);
    map.put("Level", 2);
    return map;
  }
}