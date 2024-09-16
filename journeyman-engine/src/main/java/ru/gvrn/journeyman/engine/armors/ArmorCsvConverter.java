package ru.gvrn.journeyman.engine.armors;

import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import javax.annotation.PostConstruct;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ArmorCsvConverter {
  private static final String ID = "id";
  public static final String ITEM_NAME = "Name";
  public static final String ITEM_COST = "Cost";
  public static final String ITEM_WEIGHT = "Weight";
  public static final String ITEM_SIZE = "Size";

  public static final String ARMOR_ARMOR = "Armor";
  public static final String ARMOR_MAX_DEX_BONUS = "Max Dex Bonus";
  private static final String[] ARMOR_PROPERTY_NAMES = new String[]{ITEM_NAME, ITEM_COST, ITEM_WEIGHT,
      ARMOR_ARMOR, ARMOR_MAX_DEX_BONUS
  };

  @Getter
  private final Map<Long, OutfitItem> idOutfitItemMap = new HashMap<>();

  @Value("dnd_armors.csv")
  private ClassPathResource outfitItemsResource;

  @PostConstruct
  void parse() throws IOException {
    List<String> headers = new LinkedList<>(Arrays.asList(ARMOR_PROPERTY_NAMES));
    headers.add(0, ID);
    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(headers.toArray(String[]::new))
        .setSkipHeaderRecord(true)
        .build();
    Iterable<CSVRecord> records = csvFormat.parse(new FileReader(outfitItemsResource.getFile()));
    for (CSVRecord record : records) {
      Long id = Long.valueOf(record.get(ID));
      String name = record.get(ITEM_NAME);
      Integer cost = Integer.valueOf(record.get(ITEM_COST));
      Integer weight = Integer.valueOf(record.get(ITEM_WEIGHT));
      Integer armor = Integer.valueOf(record.get(ARMOR_ARMOR));
      Integer maxDexBonus = Integer.valueOf(record.get(ARMOR_MAX_DEX_BONUS));
      Map<String, Property<?>> properties = new LinkedHashMap<>();
      properties.put(ID, createProperty(ID, id));
      properties.put(ITEM_NAME, createProperty(ITEM_NAME, name));
      properties.put(ITEM_COST, createProperty(ITEM_COST, cost));
      properties.put(ITEM_WEIGHT, createProperty(ITEM_WEIGHT, weight));
      properties.put(ARMOR_ARMOR, createProperty(ARMOR_ARMOR, armor));
      properties.put(ARMOR_MAX_DEX_BONUS, createProperty(ARMOR_MAX_DEX_BONUS, maxDexBonus));
      OutfitItem outfitItem = new OutfitItem(id, properties);
      outfitItem.setSlotsNames(Collections.singletonList("body"));
      idOutfitItemMap.put(id, outfitItem);
    }
  }

  private Property<?> createProperty(String name, Object value) {
    if (value instanceof String) {
      PropertyValue<String> pv = new PropertyValue<>("current", (String) value);
      return new StringProperty(name, pv);
    } else if (value instanceof Integer || value instanceof Long) {
      PropertyValue<Integer> pv = new PropertyValue<>("current", ((Number) value).intValue());
      return new IntegerProperty(name, pv);
    } else {
      PropertyValue<Boolean> pv = new PropertyValue<>("current", (Boolean) value);
      return new BooleanProperty(name, pv);
    }
  }
}
