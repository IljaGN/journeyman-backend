package ru.gvrn.journeyman.dicees;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gvrn.journeyman.dicees.api.DicePool;

@Getter
@RequiredArgsConstructor
public class ConstantDicePool implements DicePool {
  private final int maxDiceEdge;
  private final int rollValue;

  @Override
  public int rollAndGetValue() {
    return rollValue;
  }
}
