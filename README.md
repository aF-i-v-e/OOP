# OOP
Описание бота:
	Ищет ближайшие мероприятния и уведомляет про них.

## Задача №1

1. 1 класс - работа с консолью, получать и выводить данныеp
    1. Функции: считывание и вывод 
    2. Методы: input, output
2. 2 класс - обработка этих данных и формирование ответа пользователю. Содержит методы на каждый case из 3 класса
    1. help – помощь, вывод доступных команд
    2. hello – приветствие пользователя
    3. chose – выбрать мероприятие, присылается ссылка на сайт для покупки билетов
    4. show – показать ближайшие мероприятия
3. 3 класс - контроллер
    1. Controller – общее название для класса и метода
    2. Архитектура: Switch, case, бесконечный цикл while
    3. Получает из 1 класса данные, вызывает соответствующий метод из 2 класса, после отправляет данные на вывод в 1 класс

Распределение:
- Антон - 2 класс
- Алиса - 1, 3 классы
