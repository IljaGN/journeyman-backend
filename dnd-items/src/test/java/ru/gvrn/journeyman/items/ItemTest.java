package ru.gvrn.journeyman.items;

import org.junit.jupiter.api.Test;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
  private static final String TEST_NAME = "Test Name";
  private static final int TEST_WEIGHT = 2000;
  private static final int TEST_COST = 543;

  @Test
  public void createItem_withCorrectProperty_OK() {
    Long expId = 1L;
    Map<String, Property<?>> properties = createProperties();

    Item item = new Item(expId, properties);

    assertEquals(expId, item.getLinealId());
    assertNotNull(item.getUuid());
    assertEquals(TEST_NAME, item.getLinealName());
    assertEquals(TEST_WEIGHT, item.getWeight());
    assertEquals(TEST_COST, item.getCost());
  }

  public static Map<String, Property<?>> createProperties() {
    Map<String, Property<?>> properties = new HashMap<>();
    properties.put("Name", new StringProperty("Name", new PropertyValue<>("current", TEST_NAME)));
    properties.put("Weight", new IntegerProperty("Weight", new PropertyValue<>("current", TEST_WEIGHT)));
    properties.put("Cost", new IntegerProperty("Cost", new PropertyValue<>("current", TEST_COST)));
    return properties;
  }
}
