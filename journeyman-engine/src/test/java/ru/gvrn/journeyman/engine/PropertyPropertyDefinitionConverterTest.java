package ru.gvrn.journeyman.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PropertyPropertyDefinitionConverterTest {
  @ParameterizedTest
  @MethodSource("parseOkDp")
  public void parse_OK(PropertyDefinition definition, Class<?> expPropertyType, Object expValue) {
    PropertyPropertyDefinitionConverter converter = new PropertyPropertyDefinitionConverter();
    converter.parse(List.of(definition));

    Property<?> property = converter.getAllProperties().get(definition.getName());

    assertNotNull(property);
    assertEquals(definition.getName(), property.getName());
    assertEquals(expPropertyType, property.getClass());
    assertEquals(expValue, property.getValue());
  }

  @Test
  public void getAllProperties_OK() {
    String expValue = "test_value";
    PropertyDefinition definition = new PropertyDefinition("String", "1", expValue);
    PropertyPropertyDefinitionConverter converter = new PropertyPropertyDefinitionConverter();
    converter.parse(List.of(definition));

    Property<?> property1 = converter.getAllProperties().get(definition.getName());
    Property<?> property2 = converter.getAllProperties().get(definition.getName());

    assertNotNull(property2);
    assertEquals(definition.getName(), property2.getName());
    assertEquals(StringProperty.class, property2.getClass());
    assertEquals(expValue, property2.getValue());
    assertNotSame(property1, property2);
  }

  private static Stream<Arguments> parseOkDp() {
    return Stream.of(
        Arguments.of(new PropertyDefinition("String", "1", "test_value"), StringProperty.class, "test_value"),
        Arguments.of(new PropertyDefinition("Integer", "2", "5"), IntegerProperty.class, 5),
        Arguments.of(new PropertyDefinition("Boolean", "3", "true"), BooleanProperty.class, true),
        Arguments.of(new PropertyDefinition("Boolean", "3", "TRUE"), BooleanProperty.class, true),
        Arguments.of(new PropertyDefinition("Boolean", "3", "FaLsE"), BooleanProperty.class, false)
    );
  }
}
