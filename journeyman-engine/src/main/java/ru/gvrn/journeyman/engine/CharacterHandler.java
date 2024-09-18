package ru.gvrn.journeyman.engine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.characters.BodyPart;
import ru.gvrn.journeyman.characters.Character;
import ru.gvrn.journeyman.properties.api.Property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CharacterHandler {
  private static final Map<String, String> BODY_PART_DEPENDENCY_MAP = new HashMap<>();

  static {
    BODY_PART_DEPENDENCY_MAP.put("head", "head");
    BODY_PART_DEPENDENCY_MAP.put("left_hand", "hand");
    BODY_PART_DEPENDENCY_MAP.put("right_hand", "hand");
    BODY_PART_DEPENDENCY_MAP.put("body", "body");
    BODY_PART_DEPENDENCY_MAP.put("legs", "leg");
  }

  private final PropertyHandler propertyHandler;

  public Character createCharacter() {
    Map<String, Property<?>> namePropertyMap = propertyHandler.getPropertyMap();
    Body body = new Body();
    getBodyParts().forEach(body::add);
    return new Character(namePropertyMap, body);
  }

  private List<BodyPart> getBodyParts() {
    return BODY_PART_DEPENDENCY_MAP.entrySet().stream()
        .map(es -> new BodyPart(es.getKey(), es.getValue()))
        .collect(Collectors.toList());
  }
}
