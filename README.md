# Открытые статьи (Compose Multiplatform)

Каталог научных статей открытого доступа. Источник данных — [DOAJ](https://doaj.org) (Directory of Open Access Journals).

Курс «Кроссплатформенная разработка», группа ПИН-252т.  
Веха **В1 — Каркас**, лабораторная **ЛР1 (CMP)**.

В этой вехе сети нет: 24 мок-статьи с теми же полями, что ожидаются от API на В2. Тема и язык живут только в памяти сессии.

## Запуск

Ведущий таргет вехи — desktop:

```bash
./gradlew :desktopApp:run
```

Один раз бонусом можно проверить web:

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Паритет ключей локализации (ru / en):

```bash
python tools/check-strings.py
```

## Что сделано по ТЗ ЛР1

| Требование | Как закрыто |
|------------|-------------|
| Каркас не рассыпается при теме, языке, переходе список ↔ деталь | Состояние в `App` + Navigation 3 |
| Моки ≥ 20, поля как у источника | 24 статьи в `MockArticles.kt`, модель `Article` |
| Navigation 3, маршрут — объект | `sealed interface Screen` (`ArticleList` / `ArticleDetail(id)`) |
| Сборка JetBrains `navigation3-ui` | `org.jetbrains.androidx.navigation3:navigation3-ui` |
| Возврат выглядит как возврат | `popTransitionSpec`, `predictivePopTransitionSpec` |
| Подписи в `composeResources`, обе локали | `values` + `values-ru`, скрипт сверки |
| Тема из токенов `MaterialTheme` | `Theme.kt`, в UI только `colorScheme` |
| Экран из состояния (state + колбэки) | `ArticleListScreen` / `ArticleDetailScreen` |
| Отчёт Compose Compiler | `reportsDestination` в `shared/build.gradle.kts` |

### Что сознательно не делается (по ТЗ)

- не покедекс и не чужой домен;
- нет сохранения темы/языка на диск (это В3);
- нет адаптива под ширину окна и мобильную вёрстку (В3);
- нет deep links и привязки к адресной строке (В3);
- не переводится содержимое каталога — только интерфейс.

## Структура `shared/src/commonMain`

```
App.kt          тема, язык, навигация (Navigation 3)
domain/         модель Article, интерфейс ArticleRepository
data/           моки и MockArticleRepository
navigation/     sealed interface Screen
theme/          цветовые схемы MaterialTheme
locale/         смена языка ресурсов (expect/actual)
ui/list/        список: состояние, намерения, ViewModel, экран
ui/detail/      деталь: состояние, ViewModel, экран
ui/components/  карточка статьи, шапка, форматирование
composeResources/  строки en (values) и ru (values-ru)
```

Модуль `desktopApp` — тонкая оболочка окна; общий UI и логика в `shared`.
