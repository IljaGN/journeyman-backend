package ru.gvrn.journeyman.engine.attack;

import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.damages.api.Damage;
import ru.gvrn.journeyman.dicees.api.DicePool;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class dndAttack implements Attack {
  private final DicePool D20;

  private final int attackBonus;
  private final List<Damage> damage;

  @Override
  public void assault(Character attacked) {
    // attackedCharacter.getCharacteristics()
    int AC = 15;

    int d20Value = D20.rollAndGetValue();
    // параметр доп бонуса/штрафа
    if (d20Value == 20 || d20Value + attackBonus >= AC) {
      for (Damage dmg : damage) {
        if (dmg.isCritical(d20Value)) {
          d20Value = D20.rollAndGetValue();
          // вероятно бонуса/штраф тот же, но не факт
          if (d20Value == 20 || d20Value + attackBonus >= AC) {
            dmg.causeCritical(Collections.emptyMap());
            continue;
          }
        }
        dmg.cause(Collections.emptyMap());
      }
    }
  }
}
