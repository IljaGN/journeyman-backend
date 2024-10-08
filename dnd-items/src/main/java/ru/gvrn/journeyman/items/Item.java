package ru.gvrn.journeyman.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;

import java.util.Map;
import java.util.UUID;

// Просто предметы, монеты они вроде не экипируются и просто лежат в инвентаре, у них два свойства цена и вес
@RequiredArgsConstructor
public class Item {
  @Getter
  protected final Long linealId;
  @Getter
  protected final String uuid;
  protected final Map<String, Property<?>> properties; // linealName, name
  protected final StringProperty linealName;
  protected final IntegerProperty weight;
  protected final IntegerProperty cost;

  public Item(Long id, Map<String, Property<?>> properties) {
    this.linealId = id;
    this.uuid = UUID.randomUUID().toString();
    this.properties = properties;
    this.linealName = (StringProperty) properties.get("Name");
    this.weight = (IntegerProperty) properties.get("Weight");
    this.cost = (IntegerProperty) properties.get("Cost");
  }

  public String getLinealName() {
    return linealName.getValue();
  }

  public Integer getWeight() {
    return weight.getValue();
  }

  public Integer getCost() {
    return cost.getValue();
  }
}
