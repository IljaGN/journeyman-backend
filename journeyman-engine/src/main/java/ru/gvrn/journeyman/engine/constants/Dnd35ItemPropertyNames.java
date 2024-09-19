package ru.gvrn.journeyman.engine.constants;

public class Dnd35ItemPropertyNames {
  public static final String ID = "id";
  public static final String ITEM_NAME = "Name";
  public static final String ITEM_COST = "Cost";
  public static final String ITEM_WEIGHT = "Weight";
  public static final String ITEM_SIZE = "Size";

  public static final String ARMOR_ARMOR = "Armor";
  public static final String ARMOR_MAX_DEX_BONUS = "Max Dex Bonus";
  public static final String[] ARMOR_PROPERTY_NAMES = new String[]{ITEM_NAME, ITEM_COST, ITEM_WEIGHT,
      ARMOR_ARMOR, ARMOR_MAX_DEX_BONUS
  };

  public static final String WEAPON_DAMAGE = "Damage";
  public static final String WEAPON_DAMAGE_TYPE = "Damage Type";
  public static final String WEAPON_CRITICAL_RANGE = "Critical Range";
  public static final String WEAPON_CRITICAL_MULTIPLIER = "Critical Multiplier";
  public static final String WEAPON_RANGE_INCREMENT = "Range Increment";
  private static final String[] WEAPON_PROPERTY_NAMES = new String[]{ITEM_NAME, ITEM_COST, ITEM_WEIGHT,
      ITEM_SIZE, WEAPON_DAMAGE, WEAPON_DAMAGE_TYPE, WEAPON_CRITICAL_RANGE, WEAPON_CRITICAL_MULTIPLIER, WEAPON_RANGE_INCREMENT//, "Body Slots Tag"
  };
}
