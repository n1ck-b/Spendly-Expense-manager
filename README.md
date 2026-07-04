![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)

# Expense Tracker

Приложение для учёта личных расходов с категориями, аналитикой и поддержкой тёмной/светлой темы.
---

## Скриншоты

| Категории | История | Аналитика |
|-----------|---------|-----------|
| ![Категории](docs/screen_categories.jpg) | ![История](docs/screen_history.jpg) | ![Аналитика](docs/screen_analytics.jpg) |

<details>
<summary>Тёмная тема</summary>

| Категории | История | Аналитика |
|-----------|---------|-----------|
| ![Категории dark](docs/screen_categories_dark.jpg) | ![История dark](docs/screen_history_dark.jpg) | ![Аналитика dark](docs/screen_analytics_dark.jpg) |

</details>

<details>
<summary>Диалоги</summary>

**Управление расходами**

<img src="docs/dialog_add_expense.jpg" width="450">

**Управление категориями**

| Добавить категорию | Выбор иконки | Выбор цвета |
|-------------------|-------------|------------|
| ![Добавить категорию](docs/dialog_add_category.jpg) | ![Выбор иконки](docs/dialog_icon_picker.jpg) | ![Выбор цвета](docs/dialog_color_picker.jpg) |

</details>

---

## Возможности

- **Категории** — создание категорий с выбором иконки и цвета, отображение суммы расходов по каждой категории
- **История** — список расходов за выбранный период времени
- **Аналитика** — donut-диаграмма и разбивка по категориям
- **Фильтр по периоду** — сегодня, неделя, месяц, год
- **Валюты** — выбор валюты для каждого расхода и перевод с помощью [ExchangeRate-API](https://www.exchangerate-api.com/docs/overview)
- **Тёмная и светлая тема** — следует системной настройке

---

## Стек

|  | Технологии |
|------|-----------|
| UI | Jetpack Compose |
| Архитектура | MVVM, Clean Architecture |
| DI | Hilt |
| База данных | Room |
| Навигация | Navigation Compose |
| Диаграммы | ComposeCharts |
| Async | Kotlin Coroutines, Flow |
| Работа с сетью | Retrofit |

---

## Архитектура

```
app/
├── data/
│   ├── api/                    # внешний API
│   ├── models/
│   │   └── relations/          # связи между таблицами Room
│   └── repository/             # реализации репозиториев
├── di/                         # Hilt модуль
├── domain/
│   ├── entities/               # модели уровня Domain
│   └── useCases/
└── presentation/
    ├── components/             # переиспользуемые Compose компоненты
    ├── navigation/             # Navigation Compose: граф навигации
    ├── screens/
    │   ├── categories/         # экран категорий
    │   ├── expenses/           # экран истории расходов
    │   └── statistics/         # экран аналитики
    └── ui.theme/               # Material Theme, цвета, типографика
```

Поток данных:
`UI → ViewModel → UseCase → Repository → Room → StateFlow → UI`

**Shared ViewModel для фильтра периода времени.** Три экрана (категории, история, статистика) показывают данные за один и тот же период. Вместо того чтобы передавать фильтр через навигацию или хранить в каждой ViewModel отдельно — используется одна `SharedViewModel` на уровне Activity.
