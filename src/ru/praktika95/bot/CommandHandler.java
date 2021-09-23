package ru.praktika95.bot;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<Integer, String> events = new HashMap<Integer, String>();

    public String commandHandler(String[] commandAndArgument) {
        String botCommand = commandAndArgument[0];
        int numberEvent = commandAndArgument.length > 1 ? Integer.parseInt(commandAndArgument[1]) : -1;
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

    private String help() {
        initDictionary();
        return "Доступные команды:\nhelp - узнать список доступных команд.\nhello - получить приветственное сообщение.\nshow - узнать ближайшие мероприятия.\nchoose \"номер мероприятия\" - выбрать мероприятие.";
    }

    private String hello() {
        initDictionary();
        return "Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.";
    }

    private String show() {
        initDictionary();
        return "1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.";
    }

    private String choose(int numberEvent) {
        initDictionary();
        if (numberEvent == -1)
            return "Вы ввели некорректный номер мероприятия.";
        if (events.containsKey(numberEvent))
            return events.get(numberEvent);
        return "Такого мероприятия не существует.";
    }

    private String other() {
        initDictionary();
        return "Введённой команды не существует, вы можете выполнить команду help, чтобы узнать список доступных команд.";
    }

    private void initDictionary(){
        if (events.size() == 0){
            events.put(1, "Информация первого мероприятия.");
            events.put(2, "Информация второго мероприятия.");
            events.put(3, "Информация третьего мероприятия.");
        }
    }
}
