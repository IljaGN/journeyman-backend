package ru.gvrn.journeyman.engine.weapons;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.items.DamagingOutfitItem;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import javax.annotation.PostConstruct;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class WeaponCsvConverter {
  private static final String ID = "id";
  public static final String ITEM_NAME = "Name";
  public static final String ITEM_COST = "Cost";
  public static final String ITEM_WEIGHT = "Weight";
  public static final String ITEM_SIZE = "Size";

  public static final String WEAPON_DAMAGE = "Damage";
  public static final String WEAPON_DAMAGE_TYPE = "Damage Type";
  public static final String WEAPON_CRITICAL_RANGE = "Critical Range";
  public static final String WEAPON_CRITICAL_MULTIPLIER = "Critical Multiplier";
  public static final String WEAPON_RANGE_INCREMENT = "Range Increment";
  private static final String[] WEAPON_PROPERTY_NAMES = new String[]{ITEM_NAME, ITEM_COST, ITEM_WEIGHT,
      ITEM_SIZE, WEAPON_DAMAGE, WEAPON_DAMAGE_TYPE, WEAPON_CRITICAL_RANGE, WEAPON_CRITICAL_MULTIPLIER, WEAPON_RANGE_INCREMENT//, "Body Slots Tag"
  };

  private final Map<Long, Supplier<DamagingOutfitItem>> idWeaponMap = new HashMap<>();

  @Value("dnd_weapons.csv")
  private ClassPathResource weaponsResource;

  @PostConstruct
  void parse() throws IOException {
    List<String> headers = new LinkedList<>(Arrays.asList(WEAPON_PROPERTY_NAMES));
    headers.add(0, ID);
    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(headers.toArray(String[]::new))
        .setSkipHeaderRecord(true)
        .build();
    Iterable<CSVRecord> records = csvFormat.parse(new FileReader(weaponsResource.getFile()));
    for (CSVRecord record : records) {
      Long id = Long.valueOf(record.get(ID));
      idWeaponMap.put(id, () -> {
        String name = record.get(ITEM_NAME);
        Integer cost = Integer.valueOf(record.get(ITEM_COST));
        Integer weight = Integer.valueOf(record.get(ITEM_WEIGHT));
        String size = record.get(ITEM_SIZE);
        String damage = record.get(WEAPON_DAMAGE);
        Map<String, Property<?>> properties = new LinkedHashMap<>();
        properties.put(ID, createProperty(ID, id));
        properties.put(ITEM_NAME, createProperty(ITEM_NAME, name));
        properties.put(ITEM_COST, createProperty(ITEM_COST, cost));
        properties.put(ITEM_WEIGHT, createProperty(ITEM_WEIGHT, weight));
        properties.put(ITEM_SIZE, createProperty(ITEM_SIZE, size));
        properties.put(WEAPON_DAMAGE, createProperty(WEAPON_DAMAGE, damage));
        DamagingOutfitItem weapon = new DamagingOutfitItem(id, properties);
        weapon.setSlotIds(List.of("hand"));
        if ("Longspear".equals(name)) {
          weapon.setSlotIds(List.of("hand", "hand"));
        }
        return weapon;
      });
    }
  }

  public DamagingOutfitItem getWeaponById(Long id) {
    return idWeaponMap.get(id).get();
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
