package ru.gvrn.journeyman.characters;

import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observable;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.outfits.api.Outfitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Body implements Outfitter<OutfitItem>, Observable {
  private final List<BodyPart> parts = new ArrayList<>(); // Возможно нужен класс BodyParts в котором и лежит List<BodyPart>

  public void add(BodyPart part) {
    parts.add(part);
  }

  public BodyPart remove(String name) {
    return parts.stream()
        .filter(bp -> bp.getName().equals(name))
        .findFirst().orElseThrow(); // TODO: exception
  }

  @Override
  public List<OutfitItem> equip(List<OutfitItem> items) {
    // нужно следить за броней и щитом и при их изменении вызывать notify
    return items.stream()
        .flatMap(it -> it.putOn(parts).stream())
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public List<OutfitItem> unequip(List<String> uuid) {
    // нужно следить за броней и щитом и при их изменении вызывать notify
    return parts.stream()
        .filter(bp -> !bp.isFree())
        .map(bp -> bp.unequip(uuid))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public void add(Observer observer) {

  }

  @Override
  public void delete(Observer observer) {

  }

  @Override
  public void deleteObservers() {

  }

  @Override
  public void notify(Info info) {

  }
}
