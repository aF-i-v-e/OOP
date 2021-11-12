# OOP
Описание бота: Ищет ближайшие мероприятия и уведомляет про них.

## Задача №1

1. 1 класс - работа с консолью, получать и выводить данные
    1. Функции: считывание и вывод 
    2. Методы: input, output
2. 2 класс - обработка этих данных и формирование ответа пользователю. Содержит методы на каждый case из 3 класса
    1. help – помощь, вывод доступных команд
    2. hello – приветствие пользователя
    3. choose – выбрать мероприятие, присылается ссылка на сайт для покупки билетов
    4. show – показать ближайшие мероприятия
3. 3 класс - контроллер
    1. Controller – общее название для класса и метода
    2. Архитектура: Switch, case, бесконечный цикл while
    3. Получает из 1 класса данные, вызывает соответствующий метод из 2 класса, после отправляет данные на вывод в 1 класс

Распределение:
- Антон - 2 класс
- Алиса - 1, 3 классы

1. Убрать static у методов ConsoleOperations, в контроллере создать объект этого класса и все методы у него вызвать.
2. Тоже сделать для класса CommandHandler.
3. Логику из контроллера перенести в CommandHandler.

## Задача №2
Сделать к практике 30.09.21

1. Возвращаемые данные из обработчика сделать отдельным классом botResponse.
2. Парсинг.
3. Организовать подключение телеграмма, создать бота через botFather.
4. Перенос существующей логики на телеграмм.

Распределение:
- Антон - 2, 4
- Алиса - 1, 3

Замечания:
1. Сделать BotRequest в FormBotResponse 
2. Переименовать FormBotResponse в BotRequestHandler
3. Внесение токена и имени бота в bot.properties
4. Переделать switch case в CommandHandler в методе choosePeriod (вынести в Map, в конструкцию ключ - функция).

Распределение:
- Алиса - 1, 2
- Антон - 3, 4

## Задача №3

1. Картинка с ЕКБ, Текст: "Я бот...", Кнопки: "Мероприятия"(show), "Помощь"(help).
2. Текст: "Выберите дату", Кнопки: "Сегодня", "Завтра", "На этой неделе" и т.д.
3. Текст: "Выберите категорию", Кнопки: "Театр", "Кино", "Концерт", "Все мероприятия".
4. Список мероприятий(максимум 6 штук) и кнопка "Показать ещё"(next), покажет
следующие 6 мероприятий.
5. LinkedHashMap (сохранение порядка по добавлению элементов)
6. Переписать Buttons на Enum
7. Перенести createButtons в Bot

Распределение:
- Алиса - 1, 2
- Антон - 3, 4

14.10.21
Распределение:
- Алиса - 5
- Антон - 6, 7

Замечания:
1. Заменить на константы
2. Заменить }} на map.of в parsing

## Задача №4

1. При нажатии на мероприятия выведется отдельно само мероприятие с кнопками
   "Перейти на мероприятие"(ссылка), "Подписаться на мероприятие" и
   "Купить фальшивый QR-код"(текст: Надеюсь, вас не посадят)
2. Добавление и подключение базы данных.
3. Тесты.
4. Выделить sendMessage и sendPhoto в отдельный класс
5. Разобрать историю git
6. Переезд на Hibernate
7. Переименовать константы (number -> name)

Распределение:
- Алиса - 1, 3, 4
- Антон - 2, 5

## Задача №5

1. После кнопки "Подписаться на мероприятие" будут выданы кнопки: за день, за неделю и т.д.
2. Добавление функционала подписки на мероприятие
   После кнопок за день, за неделю и т.д. подписать на мероприятие.

Распределение:
- Алиса - 1
- Антон - 2

## Задача №6

1. Кнопка назад(текущее состояние минус один).
2. Довести до конца(последние штрихи).
3. Посмотреть про планировщики задач в java (уведомлять пользователя с помощью параллельного потока, лучше через планировщика задач т.к. больше функционала)

Распределение:
- Алиса - 2
- Антон - 1