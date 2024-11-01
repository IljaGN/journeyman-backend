package ru.gvrn.journeyman.engine;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.gvrn.journeyman.characters.Body;
import ru.gvrn.journeyman.characters.Character;
import ru.gvrn.journeyman.engine.armors.OutfitItemHolder;
import ru.gvrn.journeyman.engine.properties.PropertyHolder;
import ru.gvrn.journeyman.items.Item;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.gvrn.journeyman.engine.constants.Dnd35ChPropertyNames.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterTest {
  private OutfitItem studdedLeather;
  private OutfitItem scaleMail;
  private OutfitItem fullPlate;

  @Autowired
  private OutfitItemHolder itemHolder;

  @Autowired
  private BodyHolder bodyHolder;
  @Autowired
  private PropertyHolder propertyHandler;

  @BeforeAll
  void beforeAllTests() {
    studdedLeather = itemHolder.getOutfitItemById(7L);
    scaleMail = itemHolder.getOutfitItemById(8L);
    fullPlate = itemHolder.getOutfitItemById(9L);
    // что была возможность экипировать
    studdedLeather.setSlotIds(List.of("hand"));
    scaleMail.setSlotIds(List.of("leg"));
  }

  @ParameterizedTest
  @MethodSource("addInInventoryOkDp")
  public void addInInventory_editWeight_OK(List<Item> items, int expCarryingCapacity) {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);

    character.addInInventory(items);

    assertEquals(expCarryingCapacity, characteristics.get(CARRYING_CAPACITY).getValue());
  }

  @ParameterizedTest
  @MethodSource("removeFormInventoryOkDp")
  public void removeFormInventory_editWeight_OK(List<Item> preEquipItems, List<String> unequipUuids, int expCarryingCapacity) {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);
    character.addInInventory(preEquipItems);
    List<Item> expUnequipItems = preEquipItems.stream()
        .filter(item -> unequipUuids.contains(item.getUuid()))
        .collect(Collectors.toList());

    List<Item> unequpItems = character.removeFormInventory(unequipUuids);

    assertEquals(expCarryingCapacity, characteristics.get(CARRYING_CAPACITY).getValue());
    assertEquals(expUnequipItems, unequpItems);
  }

  @Test
  public void equip_OK() {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);
    int expCarryingCapacity = scaleMail.getWeight() + fullPlate.getWeight();
    int expArmorClass = 10 + scaleMail.getArmor() + fullPlate.getArmor();

    List<OutfitItem> unequip = character.equip(List.of(scaleMail, fullPlate));

    assertTrue(unequip.isEmpty());
    assertEquals(expCarryingCapacity, characteristics.get(CARRYING_CAPACITY).getValue());
    assertEquals(expArmorClass, characteristics.get(ARMOR_CLASS).getValue());
  }

  @Test
  public void unequip_OK() {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);
    int expCarryingCapacity = studdedLeather.getWeight() + scaleMail.getWeight() + fullPlate.getWeight();
    character.equip(List.of(studdedLeather, scaleMail, fullPlate));

    List<OutfitItem> unequip = character.unequip(List.of(scaleMail.getUuid(), fullPlate.getUuid()));

    assertEquals(List.of(scaleMail, fullPlate), unequip);
    assertEquals(expCarryingCapacity, characteristics.get(CARRYING_CAPACITY).getValue());
    assertEquals(studdedLeather.getArmor() + 10, characteristics.get(ARMOR_CLASS).getValue());
  }

  @Test
  public void levelUp_OK() {
    Map<String, Property<?>> characteristics = new HashMap<>();
    createCharacterAndWatch(characteristics);
    IntegerProperty level = (IntegerProperty) characteristics.get(LEVEL);
    Property<?> hitPoints = characteristics.get(TOTAL_HIT_POINTS);
    for (int lvl = 1; lvl < 5; lvl++) {
      int expHitPointsValue = 8;
      expHitPointsValue += 5*(lvl - 1);

      level.replaceValue(lvl);
      hitPoints.resetOnDefault(); // current == max(observe -> level)

      assertEquals(expHitPointsValue, hitPoints.getValue());
    }
  }

  @Test
  public void armor_limitForDexBonus_OK() {
    Map<String, Property<?>> characteristics = new HashMap<>();
    Character character = createCharacterAndWatch(characteristics);
    Property<?> dex = characteristics.get(DEX);
    Property<?> armorClass = characteristics.get(ARMOR_CLASS);
    int expNakedArmorClassValue = 14; // DEX(Dexterity 18) = 4
    int expFullPlateArmorClassValue = 10 + fullPlate.getArmor() + fullPlate.getMaxDexBonus();

    ((IntegerProperty) characteristics.get(DEXTERITY)).replaceValue(18);

    assertEquals(4, dex.getValue());
    assertEquals(expNakedArmorClassValue, armorClass.getValue());

    character.equip(List.of(fullPlate));

    assertEquals(4, dex.getValue());
    assertEquals(expFullPlateArmorClassValue, armorClass.getValue());
  }

  private Stream<Arguments> addInInventoryOkDp() {
    return Stream.of(
        Arguments.of(List.of(), 0),
        Arguments.of(List.of(studdedLeather), studdedLeather.getWeight()),
        Arguments.of(List.of(scaleMail), scaleMail.getWeight()),
        Arguments.of(List.of(studdedLeather, fullPlate),
            studdedLeather.getWeight() + fullPlate.getWeight()),
        Arguments.of(List.of(studdedLeather, scaleMail, fullPlate),
            studdedLeather.getWeight() + scaleMail.getWeight() + fullPlate.getWeight())
    );
  }

  private Stream<Arguments> removeFormInventoryOkDp() {
    return Stream.of(
        Arguments.of(List.of(), List.of(), 0),
        Arguments.of(List.of(studdedLeather), List.of(), studdedLeather.getWeight()),
        Arguments.of(List.of(studdedLeather), List.of("uuid1, uuid2"), studdedLeather.getWeight()),
        Arguments.of(List.of(studdedLeather, scaleMail, fullPlate),
            List.of(studdedLeather.getUuid(), scaleMail.getUuid(), fullPlate.getUuid()), 0),
        Arguments.of(List.of(studdedLeather, scaleMail, fullPlate),
            List.of(scaleMail.getUuid(), "uuid1", "uuid2"), studdedLeather.getWeight() + fullPlate.getWeight())
    );
  }

  private Character createCharacterAndWatch(Map<String, Property<?>> characteristics) {
    Body body = bodyHolder.getBody();
    propertyHandler.handle(body);
    characteristics.putAll(propertyHandler.getPropertyMap());
    return new Character(characteristics, body);
  }
}
