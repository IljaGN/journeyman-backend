Project Style
============================================================

- Maven модули именовать в **нижнем регистре** в стиле **`kebab‑case`**, последнее слово в имени
  модуля предпочтительно во множественном числе, если оно(мнж. число) существует и это уместно.
- Прочие файлы проекта именовать в **нижнем регистре** в стиле **`snake_case`** в том числе
  _java‑_`resources`. В отдельных случаях можно именовать разделяя слова пробелом или использовать
  верхний регистр в названии.
- Правила именования файлов можно нарушать, если специального наименования файлов требует
  определенная библиотека или технология.
- Внутреннее оформление файлов производится по стандартам принятым для этих файлов.

Java Code Style
------------------------------------------------------------

Стиль написания java‑кода проекта опирается на стандарты описанные в
Sun/Oracle [Code Conventions for the Java Programming Language](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html),
далее OJCC, с изменениями описанными ниже. А так же на идеи из книги "Clean Code: A Handbook of
Agile Software Craftsmanship" Robert C. Martin, далее RMCC. Подходы описанные в этих источниках
носят рекомендательный характер и могут нарушаться в ряде случаев.

Далее в OJCC будут внесены пояснения, исправления, изменения.

### File Organization ###

Файлы размером в 500 и более строк кода очень большие. Вероятно, стоит декомпозировать подобные
классы. Примеры организации файлов и стиля написания кода смотрите в самом проекте.

#### Package and Import Statements ####
Базовый `package` для всех файлов исходного кода проекта: `ru.gvrn.journeyman`

В импорте(`import`) указывать полное имя класса. Можно делать _"массовый импорт"_ через символ `'*'`,
когда число обычных импортов **более 9**, а число статических импортов **более 4**. Импорты разбить
на секции, каждая из которых отсортирована в лексикографическом порядке. Для отдельных библиотек,
например тестовых или утилитарных, всегда можно делать `import` через `'*'`.

Секции:
```java
package ru.gvrn.journeyman;

import all.imports;

import all.javax;

import all.java;

import static all.statics.imports;
```

Пакеты исключения:
```java
import static org.junit.jupiter.api.Assertions.*;
```

#### Class and Interface Declarations ####
Предпочитать естественный порядок объявления и группировки полей/методов в соответствии с логикой
класса и предметной области. Но если между полями/методами нет подобных связей или нужно расположить
блоки полей/методов относительно друг друга, можно придерживаться следующей схемы расположения
полей/методов класса в соответствии с их модификаторами:
```java
public
protected
private
abstract
static
final
transient
volatile
default (package-private)
synchronized
native
strictfp
```

<details>
<summary>Примеры:</summary>

```java
public static final;
protected static final;
private static final;
static final;

public static;
protected static;
private static;
static;

static init block;

public final;
protected final;
private final;
final;

public;
protected;
private;
default;

init block;

constructor;

// block for methods:
default; // как правило связаны с доп. инициализацией после конструктора, если нет то по стандарту
public;
protected;
private;
default;

public final;
protected final;
private final;
final;

public static;
protected static;
private static;
static;
```
</details>

### Indentation ###

В качестве отступа использовать **половину пробелов** как описано в [OJCC][1]. То есть два пробела
для отступа и четыре для двойного отступа.

#### Line Length ####
Общая рекомендация избегайте строк **длиннее 100** символов. Но не стоит, к примеру, беспокоится о
сточке, которая хорошо выглядит, а символ двоеточия `;` превысил лимит в этом нет ничего страшного.
Даже если превышение составляет 10 символов и это все еще выглядит уместно и читабельнее чем
переформатированная версия. Отдельное исключение составляют данные для тестов. Там превышение может
составлять до полутора символов от максимума. Так как тестовые данные удобно воспринимать как
таблицу, а лишние переходы в строках этому мешают.

#### Wrapping Lines ####
> EOF — end of line, символ или набор управляющих символов, которые интерпретируются как переход на
> следующую строку

Если выражение не помещается в одну строку, разбейте его в соответствии со следующими общими
принципами:
- EOF после запятой
- EOF перед оператором
- Предпочитайте EOF более высокого уровня EOF-у более низкого уровня
- Выровняйте перенесенную строку на том же уровне, что и предыдущая строка
- Если приведенные выше правила приводят к запутанному коду или коду, который сжимается по правому
  краю, просто сделайте удвоенный отступ

<details>
<summary>Примеры:</summary>

Вызов метода:
```java
someMethod(more args); // LONG

someMethod(expression1, expression2,
        expression3, expression4, expression5); // OK

someMethod(expression1, expression2, expression3,
        expression4, expression5); // OK

someMethod(
        longExpression1,
        longExpression2,
        longExpression3,
        longExpression4
); // OK

var = someMethod1(expression1,
                someMethod2(longExpression2,
                        longExpression3)); // BADLY

var = someMethod1(expression1,
        someMethod2(longExpression2, longExpression3)); // OK
// OR
expression4 = someMethod2(longExpression2, longExpression3);
var = someMethod1(expression1, expression4); // OK
```

Тернарный оператор:
```java
// Точно не нужно совмещать несклько тернарных операторов в однов выражении:
var = booleanExpression1 ? expression1 : booleanExpression2 ? expression2 : expression3;

// Если выражения простые для восприятия запишите в одну строку:
var = booleanExpression ? expression1 : expression2;

// Если второе выражение сложенее первого допустима следующая запись:
var = booleanExpression ? expression1
        : difficultLongExpression2;

// Если все выражения сложные или сложное условие, то лучше придерживаться варианта:
var = difficultBooleanExpression
        ? difficultLongExpression1
        : difficultLongExpression2;
```

Stream api:
```java
// Придерживайтесь следующей структуры извлечение выражения плюс stream в одной строке
// все последующие варажения(функции) с новой строки:
return suitableAcceptors.stream()
        .map(acceptor -> acceptor.equip(this))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
// Если нужно достать глубокий элемент:
return deepSource1.getDeepSource2()
        .getDeepSource3()
        .getDeepSource4().stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
```
</details>

### Declarations ###

#### Number Per Line ####
Рекомендуется одно объявление на строку:
```java
int level; // indentation level
int size; // size of table
```

Не нужно писать объединенных объявлений:
```java
int level, size; // BADLY
int foo, fooarray[]; // BADLY
```

Не нужно делать форматирование таблицей([RMCC][1]):
```java
// BADLY:
int     level;           // indentation level
int     size;            // size of table
Object  currentEntry;    // currently selected table entry
```

#### Initialization ####
Для вложенных циклов `for` используйте следующие переменные в соответствии с их уровнем
вложенности `i ➜ j ➜ k`, если название счетчика не играет роли. Не используйте символ `l`.

Если уместно используйте логическое название для счетчика:
```java
public void levelUp(Map<String, Property<?>> characteristics) {
    for (int lvl = 1; lvl < 5; lvl++) {
        characteristics.get(LEVEL).increase(lvl);
    }
}
```

#### Class and Interface Declarations ####
Объявлять внутренние классы(`inner class`) в самом конце родительского класса. Если внутренний класс
приватный и является просто контейнером для значений, можно не делать ему методы доступа
Getter/Setter, а напрямую обращаться к его полям.

### Statements ###

Во все примеры [OJCC][1] закралась ошибка первый `statements` находится на следующей строчке от
открывающей скобки и нет отступа в начале нового кодового блока:
```java
// Не так:
{statements;
...
// А так:
{
    statements;
    ...
```

#### if, if-else, if else-if else Statements ####
Предпочитать такой форме оператора `if-else`:
```java
if (condition1) {
    statements;
} else if (condition2) {
    statements;
} else {
    statements;
}
```

если консистентные условия, оператор `switch`. Если метод по сути завершается при входе в условие,
то делать выход из метода.

#### switch Statements ####
Располагать ключевое слово `default` в самом низу оператора `switch`. Если метод по сути завершается
при входе в условие, то использовать ключевое слово `return` за место `break`.

### White Space ###

#### Blank Lines ####
Устарело:
- ~~Between class and interface definitions~~
- ~~Between the local variables in a method and its first statement~~

Придерживайтесь правила если вы отступаете blank lines(см. [EOF](project_style.md#wrapping-lines))
после объявления класса отступайте так же и при его завершении. Или не делайте этого. Пример:
```java
// С blank lines
public class ExampleClass {
EOF
...
<content>
...
EOF
}
// Или без blank lines
public class ExampleClass {
...
<content>
...
}
```

Для методов интерфейсов у которых отсутствует `default implements` можно опустить blank lines.

#### Blank Spaces ####
Исключение для бинарных операторов. У операторов `'*' '/' '%'` не должно быть окружающих
пробелов [RMCC][1].
```java
int capacity = (strength.getValue()*6 - 6)/5 + 1;
```

### Naming Conventions ###

Стараться выбирать короткие имена пакетов, именовать их в **нижнем регистре**. Отдельный пакет
должен быть во множественном числе, если оно(мнж. число) существует и это уместно. Отдельные пакеты
отделяются символом `.`(имеются в виду вложенные папки). Если имя пакета состоит из двух слов и это
не 2 пакета придется написать их слитно без разделителей, но лучше такого избегать.

Не нужно в именах полей использовать маркеры, которые повторяют информацию из объявления поля.
Современные IDE передают эту информацию, другими наглядными способами.

Предпочитай длинные имена коротким для java классов/методов/полей. Длинна имени должна быть
соразмерна важности и области видимости класса/метода/поля [RMCC][1].

Стараться не использовать аббревиатуры, кроме общеупотребимых. Если в проекте естественным образом
возникает локальная аббревиатура дайте ее расшифровку в комментарии. Способ именования сущностей, в
которые входит аббревиатура такой, как если бы аббревиатура была обычным словом. Исключение из этого
правила аббревиатуры из 2 символов, однако если они начинают название поля они пишутся в нижнем
регистре. Аббревиатуры: ID, PK, FK использовать как id, pk, fk.

### Programming Practices ###

#### Special Comments ####
Используйте **TODO** или **FIXME** в начале комментария, что бы подсветить проблему. Вы так же можете
указать адресата комментария упомянув его _id_, _git user name_ или _фамилию_ после двоеточия.
```java
// TODO:<userId> <problemDescription>
// FIXME:<userId> <problemDescription>
```

[1]: project_style.md#java-code-style