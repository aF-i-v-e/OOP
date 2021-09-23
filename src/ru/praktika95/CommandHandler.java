package ru.praktika95;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    public static Map<Integer, String> events = new HashMap<Integer, String>();

    public static String commandHandler(String botCommand, Integer numberEvent) {
        String botAnswer = switch(botCommand) {
            case "choose"-> choose(numberEvent);
            case "hello"-> hello() ;
            case "help"-> help();
            case "show"-> show();
            case "start"-> hello();
            default -> other();
        };
        return botAnswer;
    }

    public static String help() {
        initDictionary();
        return "Доступные команды:\nhelp - узнать список доступных команд.\nhello - получить приветственное сообщение.\nshow - узнать ближайшие мероприятия.\nchoose \"номер мероприятия\" - выбрать мероприятие.";
    }
    public static String hello() {
        initDictionary();
        return "Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.";
    }
    public static String show() {
        initDictionary();
        return "1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.";
    }
    public static String choose(int numberEvent) {
        initDictionary();
        if (numberEvent == -1)
            return "Вы ввели некорректный номер мероприятия.";
        if (events.containsKey(numberEvent))
            return events.get(numberEvent);
        return "Такого мероприятия не существует.";
    }
    public static String other() {
        initDictionary();
        return "Введённой команды не существует, вы можете выполнить команду help, чтобы узнать список доступных команд.";
    }

    private static void initDictionary(){
        if (events.size() == 0){
            events.put(1, "Информация первого мероприятия.");
            events.put(2, "Информация второго мероприятия.");
            events.put(3, "Информация третьего мероприятия.");
        }
    }
}
