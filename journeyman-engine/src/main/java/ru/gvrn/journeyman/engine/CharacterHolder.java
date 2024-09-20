package ru.gvrn.journeyman.engine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.characters.Character;
import ru.gvrn.journeyman.engine.properties.PropertyHolder;

@Component
@RequiredArgsConstructor
public class CharacterHolder {

  private final BodyHolder bodyHolder;
  private final PropertyHolder propertyHandler;

  public Character createCharacter() {
    Body body = bodyHolder.getBody();
    propertyHandler.handle(body);
    return new Character(propertyHandler.getPropertyMap(), body);
  }
}
