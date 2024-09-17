package ru.gvrn.journeyman.engine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.characters.BodyPart;
import ru.gvrn.journeyman.characters.Character;
import ru.gvrn.journeyman.engine.armors.ArmorCsvConverter;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.properties.api.Property;

import javax.annotation.PostConstruct;

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
  private final ArmorCsvConverter armorsHolder;

  @PostConstruct
  void test() {
    Map<String, Property<?>> namePropertyMap = propertyHandler.getPropertyMap();
    Body body = new Body();
    getBodyParts().forEach(body::add);
    Character character = new Character(namePropertyMap, body);
    OutfitItem armors1 = armorsHolder.getOutfitItemById(9L);
    OutfitItem armors2 = armorsHolder.getOutfitItemById(6L);
    character.equip(List.of(armors1));
    character.unequip(List.of(armors1.getUuid()));
    int i = 0;
  }

  List<BodyPart> getBodyParts() {
    return BODY_PART_DEPENDENCY_MAP.entrySet().stream()
        .map(es -> new BodyPart(es.getKey(), es.getValue()))
        .collect(Collectors.toList());
  }
}
