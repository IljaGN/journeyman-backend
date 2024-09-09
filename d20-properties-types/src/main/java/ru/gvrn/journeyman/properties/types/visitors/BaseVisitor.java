package ru.gvrn.journeyman.properties.types.visitors;

public abstract class BaseVisitor implements Visitor {
  protected final int intValue;
  protected final String strValue;
  protected final boolean booValue;

  public BaseVisitor(int value) {
    intValue = value;
    strValue = String.valueOf(value);
    booValue = value > 0;
  }

  public BaseVisitor(String value) {
    if (value == null) { // Blank utils, trim etk
      value = "";
    } else if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
      booValue = Boolean.parseBoolean(value);
      intValue = booValue ? 1 : 0;
      strValue = value;
      return;
    }

    intValue = value.length();
    strValue = value;
    booValue = intValue > 0;
  }

  public BaseVisitor(boolean value) {
    intValue = value ? 1 : 0;
    strValue = String.valueOf(value);
    booValue = value;
  }
}
