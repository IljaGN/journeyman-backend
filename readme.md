Journeyman Backend
============================================================

Ознакомьтесь с [Project Style](doc/ref/project_style.md) см. описание
из [journeyman-engine](journeyman-engine/readme.md)

Сборка проекта
------------------------------------------------------------

Для сборки необходим JDK 11 (использовал OpenJDK). Maven (использовал версию 3.8.1). Используйте Git
для управления версиями исходного кода. Перейдите в каталог для установки/разработки
приложения и выполните следующие команды:

### Установка: ###

**Чтобы скачать по ssh**:
```bash
git clone git@github.com:IljaGN/journeyman-backend.git
```

Чтобы скачать по https:
```bash
git clone https://github.com/IljaGN/journeyman-backend.git
```

Перейдите в скачанный каталог:
```bash
cd journeyman-backend/
```

Убедитесь что нет проблем и операция прошла успешно:
```bash
git status
```

### Сборка: ###

Выполните следующие команды:
```bash
mvn clean test
```

Модули проекта:
- [d20-properties-types](d20-properties-types/readme.md)
- [dnd-items](dnd-items/readme.md)
- [journeyman-attacks-api]
- [journeyman-characters](journeyman-characters/readme.md)
- [journeyman-damages]
- [journeyman-damages-api]
- [journeyman-dicees](journeyman-dicees/readme.md)
- [journeyman-engine](journeyman-engine/readme.md)
- [journeyman-observers-api] <!-- (journeyman-observers-api/readme.md) -->
- [journeyman-outfits-api] <!-- (journeyman-outfits-api/readme.md) -->
- [journeyman-properties](journeyman-properties/readme.md)
- [journeyman-properties-api] <!-- (journeyman-properties-api/readme.md) -->
- [journeyman-support-api](journeyman-support-api/readme.md)

Данный проект ставит целью создать некий обобщенный кастомизируемый движек. Который может описывать
математику системы D&D. В общем случае математику D20-подобных систем. Первый шаг это описать
частично систему D&D 3.5, затем переключиться на описание Pathfinder 2E. Кастомизация движка идет
двумя путями. Простой путь это добавление и изменение конфигурационных файлов. Более сложный путь
это доработка и изменение кодовой базы движка. <ins>Проект **не подразумевает** наличие
пользовательского интерфейса</ins>. Но может быть интегрирован c SPA UI.