package ru.gvrn.journeyman.properties.values;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.properties.PropertyInfo;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.CURRENT_NAME;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.INITIAL_VALUE;
import static ru.gvrn.journeyman.properties.values.PropertyValueTest.NEW_VALUE;

class CalculatedPropertyValueTest {
  private static final String OBSERVED_PROPERTY_ID = "Test observed property";
  private static final Info INFO = new PropertyInfo(OBSERVED_PROPERTY_ID, INITIAL_VALUE, NEW_VALUE);

  @ParameterizedTest
  @MethodSource("setDependenciesAndUpdateOkDp")
  public void setDependenciesAndUpdate_split_OK(Function<Info, Object> dependencies) {
    CalculatedPropertyValue<Object> value = createValue();

    value.setDependencies(dependencies);
    value.update(INFO);

    assertEquals(dependencies.apply(INFO), value.getValue());
  }

  @Test
  public void setDependenciesAndUpdate_together_OK() {
    CalculatedPropertyValue<Object> value = createValue();

    value.setDependenciesAndUpdate(info -> NEW_VALUE);

    assertEquals(NEW_VALUE, value.getValue());
  }

  @Test
  public void update_ifDependenciesIsNull_FAIL() {
    String expOwnerName = "Test Owner";
    CalculatedPropertyValue<Object> value = createValue();
    value.setOwnerName(expOwnerName);

    IllegalStateException e = assertThrows(IllegalStateException.class, () -> value.update(INFO));

    String errMsg = e.getMessage();
    assertTrue(errMsg.contains(expOwnerName));
    assertTrue(errMsg.contains(CURRENT_NAME));
    assertTrue(errMsg.contains(OBSERVED_PROPERTY_ID));
  }

  private static Stream<Arguments> setDependenciesAndUpdateOkDp() {
    final Object objValue = new Object();
    return Stream.of(
        Arguments.of((Function<Info, Object>) info -> objValue),
        Arguments.of((Function<Info, Object>) Info::getNewValue)
    );
  }

  private CalculatedPropertyValue<Object> createValue() {
    return new CalculatedPropertyValue<>(CURRENT_NAME, INITIAL_VALUE);
  }
}
