package ru.gvrn.journeyman.engine.armors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.engine.BaseCsvParser;
import ru.gvrn.journeyman.engine.Converter;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.properties.api.Property;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static ru.gvrn.journeyman.engine.constants.Dnd35ItemPropertyNames.*;

@Component
public class OutfitItemHolder extends BaseCsvParser {
  private static final CsvRecordOutfitItemConverter CONVERTER = new CsvRecordOutfitItemConverter();

  private final Map<Long, Supplier<OutfitItem>> idOutfitItemProducerMap = new HashMap<>();

  @Value("dnd_armors.csv")
  private ClassPathResource outfitItemsCsvSource;

  @PostConstruct
  void parse() {
    parse(outfitItemsCsvSource);
  }

  void parse(Resource csvResource) {
    CSVFormat csvFormat = createCsvFormat(getHeadersWithId(ARMOR_PROPERTY_NAMES));
    parse(csvResource, csvFormat, csvRecord ->
        idOutfitItemProducerMap.put(extractLongId(csvRecord), () -> CONVERTER.convert(csvRecord))
    );
  }

  public OutfitItem getOutfitItemById(Long id) {
    return idOutfitItemProducerMap.get(id).get();
  }

  public static class CsvRecordOutfitItemConverter implements Converter<CSVRecord, OutfitItem> {
    @Override
    public OutfitItem convert(CSVRecord record) {
      String name = record.get(ITEM_NAME);
      Integer cost = Integer.valueOf(record.get(ITEM_COST));
      Integer weight = Integer.valueOf(record.get(ITEM_WEIGHT));
      Integer armor = Integer.valueOf(record.get(ARMOR_ARMOR));
      Integer maxDexBonus = Integer.valueOf(record.get(ARMOR_MAX_DEX_BONUS));
      Map<String, Property<?>> properties = new LinkedHashMap<>();
      properties.put(ITEM_NAME, createProperty(ITEM_NAME, name));
      properties.put(ITEM_COST, createProperty(ITEM_COST, cost));
      properties.put(ITEM_WEIGHT, createProperty(ITEM_WEIGHT, weight));
      properties.put(ARMOR_ARMOR, createProperty(ARMOR_ARMOR, armor));
      properties.put(ARMOR_MAX_DEX_BONUS, createProperty(ARMOR_MAX_DEX_BONUS, maxDexBonus));
      OutfitItem outfitItem = new OutfitItem(extractLongId(record), properties);
      outfitItem.setSlotIds(List.of("body")); // TODO:
      return outfitItem;
    }
  }
}
