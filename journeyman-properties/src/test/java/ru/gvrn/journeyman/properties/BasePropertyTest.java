package ru.gvrn.journeyman.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gvrn.journeyman.properties.api.Value;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.CURRENT_NAME;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.DEFAULT_NAME;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.INITIAL_VALUE;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.NEW_VALUE;

@ExtendWith(MockitoExtension.class)
class BasePropertyTest {
  public static final String PROPERTY_NAME = "Test Property";

  @Test
  public void createProperty_onlyCurrentValue_OK() {
    PropertyValue<Object> current = Mockito.spy(createCurrentValue());

    BaseProperty<Object> property = new TestProperty(PROPERTY_NAME, current);

    assertEquals(PROPERTY_NAME, property.getName());
    assertEquals(INITIAL_VALUE, property.getValue());
    verify(current).getInstance(DEFAULT_NAME);
    verify(current).setOwnerName("gjfgjfjg");
  }

  @ParameterizedTest
  @MethodSource("createPropertyManyValuesOkDp")
  public void createProperty_manyValues_OK(PropertyValue<Object>[] values) {
    PropertyValue<Object> current = Mockito.spy(createCurrentValue());
    for (int i = 0; i < values.length; i++) {
      values[i] = Mockito.spy(values[i]);
    }

    BaseProperty<Object> property = new TestProperty(PROPERTY_NAME, current, values);

    assertEquals(PROPERTY_NAME, property.getName());
    assertEquals(INITIAL_VALUE, property.getValue());
    verify(current, never()).getInstance(anyString());
    verify(current).setOwnerName(PROPERTY_NAME);
    Arrays.stream(values).forEach(v -> verify(v).setOwnerName(PROPERTY_NAME));
  }

  @Test
  public void resetOnDefault_OK() {
    BaseProperty<Object> property = createProperty();

    property.resetOnDefault();

    assertEquals(NEW_VALUE, property.getValue());
  }

  @Test
  public void updateDefaultOnCurrent_OK() {
    BaseProperty<Object> property = createProperty();

    property.updateDefaultOnCurrent();

    assertEquals(INITIAL_VALUE, property.getValue());
  }

  @Test
  public void initValue_OK() {
    String expValueName = "test value";
    PropertyValue<Object> current = Mockito.spy(createCurrentValue());
    BaseProperty<Object> property = new TestProperty(PROPERTY_NAME, current, createDefoltValue());

    Value<Object> newValue = property.initValue(expValueName);

    verify(current).getInstance(expValueName);
    assertEquals(expValueName, newValue.getName());
    assertEquals(INITIAL_VALUE, newValue.getValue());
  }

  private static Stream<Arguments> createPropertyManyValuesOkDp() {
    return Stream.of(
        Arguments.of((Object) new PropertyValue[]{createDefoltValue()}),
        Arguments.of((Object) new PropertyValue[]{createDefoltValue(), createValue("test", new Object())})
    );
  }

  private static BaseProperty<Object> createProperty() {
    return new TestProperty(PROPERTY_NAME, createCurrentValue(), createDefoltValue());
  }

  private static PropertyValue<Object> createCurrentValue() {
    return createValue(CURRENT_NAME, INITIAL_VALUE);
  }

  private static PropertyValue<Object> createDefoltValue() {
    return createValue(DEFAULT_NAME, NEW_VALUE);
  }

  private static PropertyValue<Object> createValue(String name, Object value) {
    PropertyValue<Object> result = new PropertyValue<>(name, value);
    result.setOwnerName(PROPERTY_NAME);
    return result;
  }

  private static class TestProperty extends BaseProperty<Object> {
    @SafeVarargs
    public TestProperty(String name, Value<Object> currentValue, Value<Object>... values) {
      super(name, currentValue, values);
    }
  }
}
