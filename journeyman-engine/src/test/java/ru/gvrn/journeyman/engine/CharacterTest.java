package ru.gvrn.journeyman.engine;

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
import ru.gvrn.journeyman.characters.Character;
import ru.gvrn.journeyman.engine.properties.PropertyHolder;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
class CharacterTest {

  @Autowired
  private BodyHolder bodyHolder;
  @Autowired
  private PropertyHolder propertyHandler;

  @Test
  public void test() {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);
  }

  private Character createCharacterAndWatch(Map<String, Property<?>> characteristics) {
    Body body = bodyHolder.getBody();
    propertyHandler.handle(body);
    characteristics.putAll(propertyHandler.getPropertyMap());
    return new Character(characteristics, body);
  }
}
