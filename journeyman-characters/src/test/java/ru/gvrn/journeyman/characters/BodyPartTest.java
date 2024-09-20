package ru.gvrn.journeyman.characters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.gvrn.journeyman.items.OutfitItem;
import ru.gvrn.journeyman.items.OutfitItemTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BodyPartTest {
  @ParameterizedTest
  @MethodSource("equipOkDp")
  public void equip_OK(OutfitItem preEquip, OutfitItem item, Boolean expPartFree) {
    BodyPart part = createBodyPart();
    part.equip(preEquip);

    OutfitItem unequip = part.equip(item);

    assertSame(expPartFree, part.isFree());
    assertSame(preEquip, unequip);
  }

  @Test
  public void unequip_OK() {
    BodyPart part = createBodyPart();
    OutfitItem expItem = createOutfitItem();
    part.equip(expItem);

    OutfitItem unequip1 = part.unequip();
    OutfitItem unequip2 = part.unequip();
    OutfitItem unequip3 = part.unequip();

    assertTrue(part.isFree());
    assertSame(expItem, unequip1);
    assertNull(unequip2);
    assertNull(unequip3);
  }

  @ParameterizedTest
  @MethodSource("getArmorAndGetMaxDexBonusOkDp")
  public void getArmorAndGetMaxDexBonus_OK(OutfitItem item, int expArmor, int expMaxDexBonus) {
    BodyPart part = createBodyPart();

    part.equip(item);

    assertEquals(expArmor, part.getArmor());
    assertEquals(expMaxDexBonus, part.getMaxDexBonus());
  }

  private static Stream<Arguments> equipOkDp() {
    return Stream.of(
        Arguments.of(null, null, true),
        Arguments.of(null, createOutfitItem(), false),
        Arguments.of(createOutfitItem(), createOutfitItem(), false)
    );
  }

  private static Stream<Arguments> getArmorAndGetMaxDexBonusOkDp() {
    return Stream.of(
        Arguments.of(null, 0, Integer.MAX_VALUE),
        Arguments.of(createOutfitItem(2, 5), 2, 5),
        Arguments.of(createOutfitItem(8, 3), 8, 3)
    );
  }

  private static BodyPart createBodyPart() {
    return new BodyPart("test body part", "slot");
  }

  private static OutfitItem createOutfitItem() {
    return createOutfitItem(0, 0);
  }

  private static OutfitItem createOutfitItem(int armor, int dexBonus) {
    return new OutfitItem(5L, OutfitItemTest.createProperties(armor, dexBonus));
  }
}
