# Открытые статьи (Compose Multiplatform)

Каталог научных статей открытого доступа.  
Источник: [DOAJ](https://doaj.org) · API: [doaj.org/api/v4/docs](https://doaj.org/api/v4/docs).

Курс «Кроссплатформенная разработка», группа ПИН-252т.  
Студент: **Дмитрий Моор**. Тема: **Статьи открытого доступа**.

## Вехи

| Веха | Лаба | Содержание |
|------|------|------------|
| В1 | ЛР1 | Каркас: тема, язык, Navigation 3, экраны из состояния, моки |
| В2 | ЛР4 | Живые данные: Ktor, kotlinx.serialization, поиск DOAJ |

Тема и язык только в памяти сессии (без диска). API-ключ не нужен.

## Запуск

Desktop (ведущий таргет, **без** Hot Reload):

```bash
./gradlew :desktopApp:run
```

Android — из Android Studio (нужен `INTERNET` в манифесте).  
Web (бонус):

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Паритет строк ru/en:

```bash
python tools/check-strings.py
```

### Замечание про ~600MB в Studio

Compose Hot Reload качает JetBrains Runtime. Пока окно приложения открыто, Gradle-задача жива и может докачивать файл; закрытие окна обрывает закачку. Hot Reload в проекте отключён — используйте `:desktopApp:run`.

## Подвох DOAJ

Без текста запроса API **не отдаёт список**.  
В поле поиска UI может быть пусто; в сеть уходит дефолтный запрос `open access`, если строка пустая.

## Критерии ЛР4

| Требование | Реализация |
|------------|------------|
| Корутины + StateFlow | ViewModel + `StateFlow` |
| `CancellationException` не глотать | проброс в `catch` |
| HTTP под Android / JVM / wasm | Ktor OkHttp / CIO / CIO-wasm |
| DTO ≠ модель экрана, `ignoreUnknownKeys` | `data/remote` → mapper → `Article` |
| Тяжёлая работа не на UI | `Dispatchers.Default` |
| Экраны: state + колбэки | list / detail / journal |
| Список, деталь, переход к журналу | Navigation 3, фильтр по ISSN |
| Android сеть | `INTERNET` в `AndroidManifest` |

## Структура данных в `shared`

```
domain/       Article, Journal, ArticleRepository
data/remote/  DoajConfig, DTO, mapper, HttpClient (expect/actual), DoajArticleRepository
data/         Mock* (не используется в App)
ui/list|detail|journal/
navigation/   ArticleList, ArticleDetail, JournalDetail
```
