package ru.gvrn.journeyman.dicees.api;

public interface DicePool {
  int getMaxDiceEdge(); // возвращает число, которое является наибольшим значением грани кубика
  int rollAndGetValue(); // делает бросок кубика и возвращает результат
  int getRollValue(); // возвращает результат последнего броска, если бросков еще не было вернет -1
}
