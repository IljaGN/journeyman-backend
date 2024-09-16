package ru.gvrn.journeyman.engine;

import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PropertyDefinitionCsvConverter {
  private static final String PROPERTY_NAME = "name";
  private static final String PROPERTY_TYPE = "type";
  private static final String PROPERTY_VALUE = "value";

  @Getter
  private final List<PropertyDefinition> propertyDefinitions = new ArrayList<>();

  @Value("dnd_properties.csv")
  private ClassPathResource propertiesResource;

  @PostConstruct
  void parse() throws IOException {
    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(PROPERTY_NAME, PROPERTY_TYPE, PROPERTY_VALUE)
        .setSkipHeaderRecord(true)
        .build();
    Iterable<CSVRecord> records = csvFormat.parse(new FileReader(propertiesResource.getFile()));
    for (CSVRecord rd : records) {
      PropertyDefinition pd = new PropertyDefinition(rd.get(PROPERTY_NAME), rd.get(PROPERTY_TYPE), rd.get(PROPERTY_VALUE));
      propertyDefinitions.add(pd);
    }
  }
}
