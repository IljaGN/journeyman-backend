package ru.gvrn.journeyman.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@BootstrapWith(DefaultTestContextBootstrapper.class)
@ContextConfiguration(classes = EngineApp.class)
class CharacterHolderTest {

  @Autowired
  private CharacterHolder characterHolder;

  @Test
  public void test() {
    assertNotNull(characterHolder.createCharacter());
  }
}
