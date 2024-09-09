package ru.gvrn.journeyman.properties.types.visitors;

public abstract class BasePropertyVisitor implements PropertyVisitor {
  protected final int intValue;
  protected final String strValue;
  protected final boolean booValue;

  public BasePropertyVisitor(int value) {
    intValue = value;
    strValue = String.valueOf(value);
    booValue = value > 0;
  }

  public BasePropertyVisitor(String value) {
    if (value == null) { // TODO: Blank utils, trim etk
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

  public BasePropertyVisitor(boolean value) {
    intValue = value ? 1 : 0;
    strValue = String.valueOf(value);
    booValue = value;
  }
}
