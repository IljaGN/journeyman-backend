package ru.gvrn.journeyman.properties.types.visitors;

import ru.gvrn.journeyman.properties.types.BooleanProperty;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.types.StringProperty;

public interface Visitor {
  void visit(IntegerProperty property);
  void visit(StringProperty property);
  void visit(BooleanProperty property);
}
