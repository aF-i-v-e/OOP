package ru.praktika95.bot;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<Integer, String> events = new HashMap<Integer, String>();

    public BotRequest commandHandler(String[] commandAndArgument) {
        String botCommand = commandAndArgument[0];
        int numberEvent = commandAndArgument.length > 1 ? Integer.parseInt(commandAndArgument[1]) : -1;
        BotRequest botRequest = new BotRequest();
        switch(botCommand) {
            case "choose"-> choose(numberEvent,botRequest);
            case "hello"-> hello(botRequest) ;
            case "help"-> help(botRequest);
            case "show"-> show(botRequest);
            case "start"-> hello(botRequest);
            default -> other(botRequest);
        };
        return botRequest;
    }

    private void help(BotRequest botRequest) {
        initDictionary();
        botRequest.setMessage("Доступные команды:\nhelp - узнать список доступных команд.\nhello - получить приветственное сообщение.\nshow - узнать ближайшие мероприятия.\nchoose \"номер мероприятия\" - выбрать мероприятие.");
    }

    private void hello(BotRequest botRequest) {
        initDictionary();
        botRequest.setMessage("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.");
    }

    private void show(BotRequest botRequest) {
        initDictionary();
        botRequest.setMessage("1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.");
    }

    private void choose(int numberEvent, BotRequest botRequest) {
        initDictionary();
        String message="Такого мероприятия не существует.";
        if (numberEvent == -1)
            message="Вы ввели некорректный номер мероприятия.";
        if (events.containsKey(numberEvent))
             message=events.get(numberEvent);
        botRequest.setMessage(message);
    }

    private void other(BotRequest botRequest) {
        initDictionary();
        botRequest.setMessage("Введённой команды не существует, вы можете выполнить команду help, чтобы узнать список доступных команд.");
    }

    private void initDictionary(){
        if (events.size() == 0){
            events.put(1, "Информация первого мероприятия.");
            events.put(2, "Информация второго мероприятия.");
            events.put(3, "Информация третьего мероприятия.");
        }
    }
}
