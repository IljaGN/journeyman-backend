package ru.gvrn.journeyman.engine.armors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.gvrn.journeyman.engine.EngineApp;
import ru.gvrn.journeyman.items.OutfitItem;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
class ArmorCsvConverterTest {

  @Autowired
  private ArmorCsvConverter converter;

  @Test
  public void loadAndParseResources_OK() {
    for (long id = 6L; id < 10L; id++) {
      assertNotNull(converter.getOutfitItemById(id));
    }
  }

  @ParameterizedTest
  @CsvSource({"7,Studded leather,2500,10000,3,5", "9,Full plate,150000,25000,8,1"})
  public void getOutfitItemById_OK(Long id, String armorName, int cost, int weight) {
    OutfitItem outfitItem = converter.getOutfitItemById(id);
    OutfitItem copyOutfitItem = converter.getOutfitItemById(id);

    assertNotNull(outfitItem.getUuid());
    assertEquals(cost, outfitItem.getCost());
    assertEquals(weight, outfitItem.getWeight());
    assertNotSame(outfitItem, copyOutfitItem);
  }
}
