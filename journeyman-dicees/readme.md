Journeyman Dicees
============================================================

Думаю из названия этого модуля все очевидно :). Обратите внимание, что минимальной единицей этого
модуля является не один кубик, а набор одинаковых кубиков. Нет реализации для кубиков с конкретным
числом граней, но оно как буд-то и не нужно. Этот модуль можно использовать в другом в качестве
основы, если такая реализация вдруг понадобиться. Вероятно сюда нужно будет добавить
`CompositeDicePool`, который будет содержать в себе пулы разногранных кубиков. Класс
`ConstantDicePool` нужен в первую очередь для упрощения тестирования объектов, которые опираются на
свойства со значением кубиков и возможно, специфичных игровых ситуаций.