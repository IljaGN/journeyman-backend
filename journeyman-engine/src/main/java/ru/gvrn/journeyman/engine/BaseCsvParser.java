package ru.gvrn.journeyman.engine;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import ru.gvrn.journeyman.engine.constants.Dnd35ItemPropertyNames;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

import static ru.gvrn.journeyman.engine.constants.Dnd35ItemPropertyNames.ID;

public abstract class BaseCsvParser {
  protected CSVFormat createCsvFormat(String... headers) {
    return CSVFormat.DEFAULT.builder()
        .setHeader(headers)
        .setSkipHeaderRecord(headers.length > 0)
        .build();
  }

  protected String[] getHeadersWithId(String... propertyNames) {
    String[] headers = new String[propertyNames.length + 1];
    System.arraycopy(propertyNames, 0, headers, 1, propertyNames.length);
    headers[0] = Dnd35ItemPropertyNames.ID;
    return headers;
  }

  protected void parse(Resource resource, CSVFormat format, Consumer<CSVRecord> consumer) {
    try (FileReader reader = new FileReader(resource.getFile())) {
      Iterable<CSVRecord> records = format.parse(reader);
      records.forEach(consumer);
      // TODO: block exception:
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected static Long extractLongId(CSVRecord record) {
    return Long.valueOf(record.get(ID));
  }

  protected static Property<?> createProperty(String name, Object value) {
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
