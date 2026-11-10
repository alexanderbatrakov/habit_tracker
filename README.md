# Habit Tracker

Трекер привычек: список с сериями и статусами, детали, создание/редактирование, каталог упражнений с публичного API [wger.de](https://wger.de/api/v2/).

## Стек

- Kotlin, Jetpack Compose, Single Activity
- MVI, Navigation Compose
- Clean Architecture
- Dagger 2, Room, Retrofit + Kotlin Serialization, Coroutines/Flow
- Тесты: JUnit + Mockito (unit), Kaspresso (UI), Detekt

## Модули

```
:app                       сборка, навигация, DI
:core:domain               модели, интерфейсы репозиториев
:core:common               MVI-инфраструктура
:core:database             Room
:core:network              Retrofit (wger API)
:core:data                 реализации репозиториев
:core:designsystem         тема, общие компоненты, строки
:feature:*                 habitslist, createhabit, edithabit, habitdetail, exercisecatalog
```

## Сборка

```bash
./gradlew assembleDebug              # приложение
./gradlew testDebugUnitTest          # unit-тесты
./gradlew connectedDebugAndroidTest  # UI-тесты (эмулятор/устройство)
./gradlew detekt                     # статический анализ
```

Для каталога упражнений нужен интернет, остальное работает офлайн.
