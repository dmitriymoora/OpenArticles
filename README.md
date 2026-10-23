# Открытые статьи (Compose Multiplatform)

Каталог научных статей открытого доступа. Источник данных — [DOAJ](https://doaj.org) (Directory of Open Access Journals).
В вехе В1 данные берутся из моков: 24 статьи с теми же полями, что приходят из API DOAJ.

Курс «Кроссплатформенная разработка», ПИН-252т.

## Запуск

```bash
./gradlew :desktopApp:run
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Проверка, что ключи строк совпадают в обеих локалях:

```bash
python tools/check-strings.py
```

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
```
