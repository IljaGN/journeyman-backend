package ru.gvrn.journeyman.properties.values;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyValueTest {
  @Test
  public void successTest() {
    System.out.printf("Run class:'%s' method '#successTest'", getClass().getSimpleName());
    System.out.println();
    String pvName = "test_name";

    PropertyValue<Object> pv = new PropertyValue<>(pvName, "value");
    assertNotNull(pv);
    assertEquals(pvName, pv.getName());
  }

  @Test
  @Disabled
  public void failedTest() {
    assertTrue(false);
  }
}
