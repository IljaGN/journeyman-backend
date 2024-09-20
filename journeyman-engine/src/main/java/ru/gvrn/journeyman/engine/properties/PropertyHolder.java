package ru.gvrn.journeyman.engine.properties;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PropertyHolder {
  private final PropertyValueDefinitionSource propertyConverter;
  private final Dnd35JavaCharacteristicsLinker characteristicsLinker;

  public void handle(Body body) {
    propertyConverter.parse();
    characteristicsLinker.bind(Dnd35JavaCharacteristicsLinker.LinkContainer.builder()
        .nameBooleanDefinitionMap(propertyConverter.getNameBooleanPropertyMap())
        .nameDicePoolDefinitionMap(propertyConverter.getNameDicePoolPropertyMap())
        .nameIntegerDefinitionMap(propertyConverter.getNameIntegerPropertyMap())
        .nameStringDefinitionMap(propertyConverter.getNameStringPropertyMap())
        .body(body)
        .build());
  }

  public Map<String, Property<?>> getPropertyMap() {
    return propertyConverter.getAllProperties();
  }
}
