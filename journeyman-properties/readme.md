Journeyman Properties
============================================================

**Свойства** — это ключевая идея этого движка. Если взглянуть на лист персонажа любых D&D систем
можно увидеть, что по факту вся представленная на нем информация это пары key➜value. Далее игровая
машина обрабатывает эти пары, чтобы делать выводы об успешности действий/заявок персонажа или
определении других цифровых значений. Из этого можно сделать вывод, что в движек должно быть легко
добавить новое _свойство_ и взаимодействие с ним других элементов системы на основании существующего
списка правил. Расширение самого списка правил отдельная не тривиальная задача.

**Вычисляемые свойства** или следящие свойства — это вторая ключевая идея движка. Это _свойства_,
которые рассчитывают свои значения на основании значений других _свойств_. И изменяют свои значения
вместе с ними. Ярким примером таких _свойств_ в D&D являются модификаторы основных характеристик
персонажа. Значит добавлять такие свойства и правила связывающие их с другими свойствами должно быть
легко. На уровне api сделать это уже легко:
```java
PropertyValue<Integer> strength;
CalculatedPropertyValue<Integer> capMax; // carrying capacity
capMax.observe(strength);
capMax.setDependenciesAndUpdate(info -> (strength.getValue()*6 - 6)*1000);
```

> [!WARNING]
> Не создавайте прямые или опосредованные циклы, когда свойство явно или нет следит за самим собой
> это приведет к зависанию приложения