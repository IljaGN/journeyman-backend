package ru.gvrn.journeyman.engine.properties;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.gvrn.journeyman.engine.EngineApp;
import ru.gvrn.journeyman.engine.properties.models.PropertyDefinition;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
class PropertyDefinitionSourceTest {

  @Autowired
  private PropertyDefinitionSource definitionSource;

  @ParameterizedTest
  @CsvSource({"String,1,test_value", "Integer,2,5", "Boolean,3,true"})
  public void getPropertyDefinitions_OK(String name, String type, String value) {
    PropertyDefinition definition = definitionSource.getPropertyDefinitions().stream()
        .filter(def -> name.equals(def.getName()))
        .findFirst().orElseThrow(() ->
            new NoSuchElementException(String.format("Element with name '%s' not found", name))
        );

    assertEquals(type, definition.getType());
    assertEquals(value, definition.getValue());
  }
}
