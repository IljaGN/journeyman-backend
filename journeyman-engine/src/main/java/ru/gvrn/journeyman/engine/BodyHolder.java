package ru.gvrn.journeyman.engine;

import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.characters.BodyPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BodyHolder {
  private static final Map<String, String> BODY_PART_DEPENDENCY_SOURCE = new HashMap<>();
  static {
    BODY_PART_DEPENDENCY_SOURCE.put("head", "head");
    BODY_PART_DEPENDENCY_SOURCE.put("left_hand", "hand");
    BODY_PART_DEPENDENCY_SOURCE.put("right_hand", "hand");
    BODY_PART_DEPENDENCY_SOURCE.put("body", "body");
    BODY_PART_DEPENDENCY_SOURCE.put("legs", "leg");
  }

  public Body getBody() {
    Body body = new Body();
    getBodyParts().forEach(body::add);
    return body;
  }

  private List<BodyPart> getBodyParts() {
    return BODY_PART_DEPENDENCY_SOURCE.entrySet().stream()
        .map(es -> new BodyPart(es.getKey(), es.getValue()))
        .collect(Collectors.toList());
  }
}
