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

1. Возвращаемые данные из обработчика сделать отдельным классом botRequest.
2. Парсинг.
3. Создать бота через botFather и подключить его к программе.
4. Добавление функционала подписки на мероприятие.

Распределение:
- Антон - 2, 4
- Алиса - 1, 3