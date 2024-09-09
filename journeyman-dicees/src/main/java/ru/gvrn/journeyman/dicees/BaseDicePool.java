package ru.gvrn.journeyman.dicees;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.gvrn.journeyman.dicees.api.DicePool;

import java.util.Random;

@RequiredArgsConstructor
public class BaseDicePool implements DicePool {
  private final Random rndm;

  @Getter
  protected final int maxDiceEdge;
  @Getter
  @Setter
  protected int diceCount = 1;
  @Getter
  protected int rollValue = -1;

  public BaseDicePool(int diceCount, int maxDiceEdge) {
    this(maxDiceEdge);
    this.diceCount = diceCount;
  }

  public BaseDicePool(int maxDiceEdge) {
    this(new Random(), maxDiceEdge);
  }

  public BaseDicePool(Random rndm, int diceCount, int maxDiceEdge) {
    this(rndm, maxDiceEdge);
    this.diceCount = diceCount;
  }

  @Override
  public int rollAndGetValue() {
    rollValue = rollDice(diceCount);
    return rollValue;
  }

  protected int rollDice(int times) {
    int result = 0;
    for (int i = 0; i < times; i++) {
      result += rollDice();
    }
    return result;
  }

  protected int rollDice() {
    return rndm.nextInt(maxDiceEdge) + 1;
  }
}
