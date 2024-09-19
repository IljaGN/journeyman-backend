package ru.gvrn.journeyman.engine.properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.engine.BaseCsvParser;
import ru.gvrn.journeyman.engine.Converter;
import ru.gvrn.journeyman.engine.properties.models.PropertyDefinition;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PropertyDefinitionSource extends BaseCsvParser {
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_TYPE = "type";
  public static final String PROPERTY_VALUE = "value";

  private static final CsvRecordPropertyDefinitionConverter CONVERTER = new CsvRecordPropertyDefinitionConverter();

  private final List<PropertyDefinition> definitions = new ArrayList<>();

  @Value("dnd_properties.csv")
  private ClassPathResource propertiesCsvSource;

  @PostConstruct
  void parse() {
    parse(propertiesCsvSource);
  }

  void parse(Resource csvResource) {
    CSVFormat csvFormat = createCsvFormat(PROPERTY_NAME, PROPERTY_TYPE, PROPERTY_VALUE);
    parse(csvResource, csvFormat, csvRecord -> definitions.add(CONVERTER.convert(csvRecord)));
  }

  public List<PropertyDefinition> getPropertyDefinitions() {
    return Collections.unmodifiableList(definitions);
  }

  public static class CsvRecordPropertyDefinitionConverter implements Converter<CSVRecord, PropertyDefinition> {
    @Override
    public PropertyDefinition convert(CSVRecord rd) {
      return new PropertyDefinition(rd.get(PROPERTY_NAME), rd.get(PROPERTY_TYPE), rd.get(PROPERTY_VALUE));
    }
  }
}
