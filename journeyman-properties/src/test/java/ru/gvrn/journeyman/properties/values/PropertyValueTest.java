package ru.gvrn.journeyman.properties.values;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.properties.PropertyInfo;
import ru.gvrn.journeyman.properties.api.Value;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PropertyValueTest {
  public static final String CURRENT_NAME = "current";
  public static final String DEFAULT_NAME = "default";

  public static final Object INITIAL_VALUE = "initial value";
  public static final Object NEW_VALUE = "new value";

  private static final Info INFO = new PropertyInfo(CURRENT_NAME, INITIAL_VALUE, NEW_VALUE);

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2}) // 0 - empty list observers
  public void notify_OK(int observerCount) {
    PropertyValue<Object> value = createValue();
    Observer[] mockObservers = createMockObservers(observerCount);
    Arrays.stream(mockObservers).forEach(value::add);

    value.notify(INFO);

    Arrays.stream(mockObservers)
        .forEach(obs -> verify(obs).update(INFO));
  }

  @Test // Так же тестирует addObserver_OK метод PropertyValue#add(Observer)
  public void setValue_OK() {
    PropertyValue<Object> value = createValue();
    Observer mockObserver = createMockObserver();
    value.add(mockObserver);

    value.setValue(NEW_VALUE);

    ArgumentCaptor<Info> infoCaptor = ArgumentCaptor.forClass(Info.class);
    verify(mockObserver).update(infoCaptor.capture());
    assertObserverInfo(infoCaptor.getValue());
    assertEquals(NEW_VALUE, value.getValue());
  }

  @Test
  public void getInstance_OK() {
    PropertyValue<Object> value = createValue();

    Value<Object> testValue = value.getInstance(DEFAULT_NAME);

    assertEquals(DEFAULT_NAME, testValue.getName());
    assertEquals(INITIAL_VALUE, testValue.getValue());
  }

  @Test
  public void deleteObserver_OK() {
    PropertyValue<Object> value = createValue();
    Observer[] mockObservers = createMockObservers(3);
    Arrays.stream(mockObservers).forEach(value::add);

    value.delete(mockObservers[1]);
    value.notify(INFO);

    verify(mockObservers[1], never()).update(INFO);
  }

  @Test
  public void deleteObservers_OK() {
    PropertyValue<Object> value = createValue();
    Observer[] mockObservers = createMockObservers(3);
    Arrays.stream(mockObservers).forEach(value::add);

    value.deleteObservers();
    value.notify(INFO);

    Arrays.stream(mockObservers)
        .forEach(obs -> verify(obs, never()).update(INFO));
  }

  private PropertyValue<Object> createValue() {
    return new PropertyValue<>(CURRENT_NAME, INITIAL_VALUE);
  }

  private Observer createMockObserver() {
    return Mockito.mock(Observer.class);
  }

  private Observer[] createMockObservers(int count) {
    Observer[] observers = new Observer[count];
    for (int i = 0; i < count; i++) {
      observers[i] = Mockito.mock(Observer.class);
    }
    return observers;
  }

  private void assertObserverInfo(Info info) {
    assertEquals(INITIAL_VALUE, info.getOldValue());
    assertEquals(NEW_VALUE, info.getNewValue());
  }
}
