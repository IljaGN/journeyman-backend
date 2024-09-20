package ru.gvrn.journeyman.characters;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.items.OutfitItemTest;
import ru.gvrn.journeyman.observers.api.Info;
import ru.gvrn.journeyman.observers.api.Observer;
import ru.gvrn.journeyman.properties.PropertyInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// TODO: данный тест не перепроверяет процесс разикипировки из теста OutfitItemTest
class BodyTest {
  private static final String HEAD_SLOT = "head";
  private static final String BODY_SLOT = "body";
  private static final String LEG_SLOT = "leg";

  private static final OutfitItem HEAD_ITEM = createOutfitItem(HEAD_SLOT);
  private static final OutfitItem BODY_ITEM = createOutfitItem(BODY_SLOT);
  private static final OutfitItem LEGS_ITEM = createOutfitItem(LEG_SLOT);

  private static final Map<String, String> BODY_PART_MAP = new HashMap<>();
  static {
    BODY_PART_MAP.put("head", HEAD_SLOT);
    BODY_PART_MAP.put("body", BODY_SLOT);
    BODY_PART_MAP.put("legs", LEG_SLOT);
  }

  @ParameterizedTest
  @MethodSource("equipOkDp")
  public void equip_OK(List<OutfitItem> items, int expUnequipItemCount) {
    Body body = createBody();

    List<OutfitItem> unequipItems = body.equip(items);

    int expEquipItemCount = items.size() - expUnequipItemCount; // неверно для предмета занимающего несколько слотов
    assertEquals(expUnequipItemCount, unequipItems.size());
    assertEquals(expEquipItemCount, body.getNonFreeBodyPartStream().count());
  }

  @ParameterizedTest
  @MethodSource("unequipOkDp")
  public void unequip_OK(List<OutfitItem> items, List<String> unequipUuids, int expUnequipItemCount) {
    Body body = createBody();
    body.equip(items);

    List<OutfitItem> unequipItems = body.unequip(unequipUuids);

    int expEquipItemCount = items.size() - expUnequipItemCount; // неверно для предмета занимающего несколько слотов
    assertEquals(expUnequipItemCount, unequipItems.size());
    assertEquals(expEquipItemCount, body.getNonFreeBodyPartStream().count());
  }

  @ParameterizedTest
  @MethodSource("getArmorClassAndGetMaxDexBonusOkDp")
  public void getArmorClassAndGetMaxDexBonus_OK(List<OutfitItem> items, int expArmorClass, int expDexBonus,
                                                Info expArmorInfo, Info expDexBonusInfo) {
    TestObserver observer = new TestObserver();
    Body body = createBody();
    observer.observe(body);

    body.equip(items);

    assertEquals(expArmorClass, body.getArmorClass());
    assertEquals(expDexBonus, body.getMaxDexBonus());
    if (Objects.nonNull(expArmorInfo)) {
      assertTrue(observer.wasReceived(expArmorInfo));
    }
    if (Objects.nonNull(expDexBonusInfo)) {
      assertTrue(observer.wasReceived(expDexBonusInfo));
    }
  }

  private static Stream<Arguments> equipOkDp() {
    String notExistSlot = "not_exist";
    return Stream.of(
        Arguments.of(List.of(), 0),
        Arguments.of(List.of(BODY_ITEM), 0),
        Arguments.of(List.of(createOutfitItem(notExistSlot)), 1),
        Arguments.of(List.of(BODY_ITEM, createOutfitItem(BODY_SLOT)), 1),
        Arguments.of(List.of(BODY_ITEM, HEAD_ITEM, LEGS_ITEM), 0),
        Arguments.of(List.of(BODY_ITEM, HEAD_ITEM, LEGS_ITEM,
            createOutfitItem(HEAD_SLOT), createOutfitItem(LEG_SLOT), createOutfitItem(notExistSlot)), 3)
    );
  }

  private static Stream<Arguments> unequipOkDp() {
    return Stream.of(
        Arguments.of(List.of(), List.of(), 0),
        Arguments.of(List.of(BODY_ITEM), List.of(), 0),
        Arguments.of(List.of(), List.of("uuid1", "uuid2"), 0),
        Arguments.of(List.of(BODY_ITEM), List.of("uuid1", "uuid2"), 0),
        Arguments.of(List.of(BODY_ITEM), List.of(BODY_ITEM.getUuid()), 1),
        Arguments.of(List.of(BODY_ITEM, HEAD_ITEM, LEGS_ITEM), List.of(HEAD_ITEM.getUuid()), 1),
        Arguments.of(List.of(BODY_ITEM, HEAD_ITEM, LEGS_ITEM), List.of(BODY_ITEM.getUuid(), HEAD_ITEM.getUuid(), LEGS_ITEM.getUuid()), 3)
    );
  }

  private static Stream<Arguments> getArmorClassAndGetMaxDexBonusOkDp() {
    OutfitItem headItem2 = createOutfitItem(2, Integer.MAX_VALUE, HEAD_SLOT);
    OutfitItem bodyItem5 = createOutfitItem(5, Integer.MAX_VALUE, BODY_SLOT);
    OutfitItem legItem3 = createOutfitItem(3, Integer.MAX_VALUE, LEG_SLOT);
    OutfitItem bodyItem03 = createOutfitItem(0, 3, BODY_SLOT);
    OutfitItem legItem15 = createOutfitItem(1, 5, LEG_SLOT);
    return Stream.of(
        Arguments.of(List.of(), 0, Integer.MAX_VALUE, null, null),
        Arguments.of(List.of(bodyItem5), 5, Integer.MAX_VALUE, createInfo(0, 5), null),
        Arguments.of(List.of(headItem2, bodyItem5, legItem3), 10, Integer.MAX_VALUE, createInfo(0, 10), null),
        Arguments.of(List.of(bodyItem03), 0, 3, null, createInfo(Integer.MAX_VALUE, 3)),
        Arguments.of(List.of(bodyItem03, legItem15), 1, 3, createInfo(0, 1), createInfo(Integer.MAX_VALUE, 3)),
        Arguments.of(List.of(headItem2, bodyItem5, legItem15), 8, 5, createInfo(0, 8), createInfo(Integer.MAX_VALUE, 5))
    );
  }

  private static Body createBody() {
    Body body = new Body();
    createBodyParts().forEach(body::add);
    return body;
  }

  private static List<BodyPart> createBodyParts() {
    return BODY_PART_MAP.entrySet().stream()
        .map(es -> new BodyPart(es.getKey(), es.getValue()))
        .collect(Collectors.toList());
  }

  private static OutfitItem createOutfitItem(String slot) {
    return createOutfitItem(0, 0, slot);
  }

  private static OutfitItem createOutfitItem(int armor, int dexBonus, String slot) {
    OutfitItem item = new OutfitItem(7L, OutfitItemTest.createProperties(armor, dexBonus));
    item.setSlotIds(List.of(slot));
    return item;
  }

  private static PropertyInfo createInfo(Object oldValue, Object newValue) {
    return new PropertyInfo(Body.class.getSimpleName(), oldValue, newValue);
  }

  private static class TestObserver implements Observer {
    private final Set<Info> infos = new HashSet<>();

    @Override
    public void update(Info info) {
      infos.add(info);
    }

    public boolean wasReceived(Info info) {
      return infos.contains(info);
    }
  }
}
