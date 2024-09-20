package ru.gvrn.journeyman.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gvrn.journeyman.outfits.api.Equippable;
import ru.gvrn.journeyman.properties.api.Property;
import ru.gvrn.journeyman.properties.types.IntegerProperty;
import ru.gvrn.journeyman.properties.values.PropertyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OutfitItemTest {
  private static final int TEST_ARMOR = 4;
  private static final int TEST_DEX_BONUS = 6;

  @Test
  public void createOutfitItem_withCorrectProperty_OK() {
    Map<String, Property<?>> properties = createProperties();

    OutfitItem item = new OutfitItem(1L, properties);

    assertEquals(TEST_ARMOR, item.getArmor());
    assertEquals(TEST_DEX_BONUS, item.getMaxDexBonus());
  }

  @ParameterizedTest
  @MethodSource("putOnOkDp")
  public void putOn_OK(List<TestEquippable> eqSlots, OutfitItem testItem, List<String> slots, int expUnequipItemCount) {
    Map<String, Property<?>> properties = createProperties();
    OutfitItem item = new OutfitItem(1L, properties);
    item.setSlotIds(slots);

    List<OutfitItem> items = item.putOn(eqSlots);

    assertEquals(expUnequipItemCount, items.size());
    if (eqSlots.isEmpty() || slots.isEmpty()) {
      assertSame(item, items.get(0));
      return;
    }

    for (TestEquippable eq : eqSlots) {
      if (eq.mustBeInstalledInTest) {
        assertSame(item, eq.item);
      } else {
        assertSame(testItem, eq.item);
      }
    }
  }

  private static Stream<Arguments> putOnOkDp() {
    String hand = "hand";
    List<String> oneHand = List.of(hand);
    List<String> twoHand = List.of(hand, hand);
    OutfitItem testItem = new OutfitItem(5L, createProperties());
    TestEquippable notInstallHand = new TestEquippable(false, hand, testItem);
    TestEquippable installHand = new TestEquippable(true, hand, testItem);
    TestEquippable installHandEmpty = new TestEquippable(true, hand, null);
    TestEquippable notInstallBody = new TestEquippable(false, "body", testItem);
    return Stream.of(
        // empty equippables -- скорее всего ошибка настроек, не заданы доступные слоты для экипировки
        Arguments.of(List.of(), testItem, oneHand, 1),
        // empty slots -- так может произойти если забыли установить слоты для предмета
        Arguments.of(List.of(notInstallHand.clone()), testItem, List.of(), 1),
        // empty equippables and slots -- на всякий пожарный :)
        Arguments.of(List.of(), testItem, List.of(), 1),
        // нормальные кейсы
        Arguments.of(List.of(installHand.clone()), testItem, oneHand, 1),
        Arguments.of(List.of(installHandEmpty.clone()), testItem, oneHand, 0),
        Arguments.of(List.of(notInstallBody.clone(), notInstallBody.clone(), installHand.clone()), testItem, oneHand, 1),
        // не установится, так как слот не подходит body vs hand
        Arguments.of(List.of(notInstallBody.clone()), testItem, oneHand, 1),
        // не установится, так как слотов требуется больше чем доступно
        Arguments.of(List.of(notInstallHand.clone()), testItem, twoHand, 1),
        // установится, с заменой занятых слотов
        Arguments.of(List.of(installHand.clone(), installHand.clone()), testItem, twoHand, 2),
        Arguments.of(List.of(installHand.clone(), installHandEmpty.clone()), testItem, twoHand, 1),
        Arguments.of(List.of(installHandEmpty.clone(), installHandEmpty.clone()), testItem, twoHand, 0),
        // подходящих слотов в избытке, должны заняться только необходимые (ОСТОРОЖНО: тесты ниже опираются на порядок элементов в коллекции)
        // List.of() имеет строгий порядок, но коллекция неоднократно преобразуется, в теории могут проваливаться
        Arguments.of(List.of(installHand.clone(), notInstallHand.clone(), notInstallHand.clone()), testItem, oneHand, 1),
        Arguments.of(List.of(installHand.clone(), installHand.clone(), notInstallHand.clone()), testItem, twoHand, 2)
    );
  }

  public static Map<String, Property<?>> createProperties() {
    return createProperties(TEST_ARMOR, TEST_DEX_BONUS);
  }

  public static Map<String, Property<?>> createProperties(int armor, int dexBonus) {
    Map<String, Property<?>> properties = new HashMap<>(ItemTest.createProperties());
    properties.put("Armor", new IntegerProperty("Armor", new PropertyValue<>("current", armor)));
    properties.put("Max Dex Bonus", new IntegerProperty("Max Dex Bonus", new PropertyValue<>("current", dexBonus)));
    return properties;
  }

  @Getter
  @AllArgsConstructor
  private static class TestEquippable implements Equippable<OutfitItem>, Cloneable {
    private boolean mustBeInstalledInTest;

    private final String slotId;
    private OutfitItem item;

    @Override
    public String getName() {
      return "test_name";
    }

    @Override
    public boolean isFree() {
      return Objects.isNull(item);
    }

    @Override
    public OutfitItem equip(OutfitItem outfitItem) {
      return replace(outfitItem);
    }

    @Override
    public OutfitItem unequip() {
      return replace(null);
    }

    @Override
    @SneakyThrows
    protected TestEquippable clone() {
      return (TestEquippable) super.clone();
    }

    private OutfitItem replace(OutfitItem newOutfit) {
      OutfitItem oldOutfit = item;
      item = newOutfit;
      return oldOutfit;
    }
  }
}
