package ru.praktika95.bot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<Integer, String> events = new HashMap<>();

    private String[] getCommandAndEventNumber(String inputText) {
        String[] commandAndArgument = inputText.split(" ");
        String botCommand = commandAndArgument[0];
        int numberEvent = commandAndArgument.length > 1 ? Integer.parseInt(commandAndArgument[1]) : -1;
        return new String[]{botCommand, Integer.toString(numberEvent)};
    }

    public BotResponse commandHandler(String input) {
        String[] commandAndEventNumber = getCommandAndEventNumber(input);
        String botCommand = commandAndEventNumber[0];
        int numberEvent = Integer.parseInt(commandAndEventNumber[1]);
        BotResponse botResponse = new BotResponse();
        int codeCategory = 0; //Пока что будет ноль
        switch(botCommand) {
            case "/choose"-> choose(numberEvent, botResponse);
            case "/hello"-> hello(botResponse) ;
            case "/help", "/start"-> help(botResponse);
            case "/show"-> show(botResponse);
            case "/chooseCategory"-> botResponse.setCategory(codeCategory);
            case "/choosePeriod"-> choosePeriod(botCommand, botResponse);
            default -> other(botResponse);
        }
        return botResponse;
    }

    private void help(BotResponse botResponse) {
        initDictionary();
        botResponse.setMessage("Доступные команды:\n/help - узнать список доступных команд.\n/hello - получить приветственное сообщение.\n/show - узнать ближайшие мероприятия.\n/choose \"номер мероприятия\" - выбрать мероприятие.");
    }

    private void hello(BotResponse botResponse) {
        initDictionary();
        botResponse.setMessage("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.");
    }

    private void show(BotResponse botResponse) {
        initDictionary();
        botResponse.setMessage("1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.");
    }

    private void choose(int numberEvent, BotResponse botResponse) {
        initDictionary();
        String message = "Такого мероприятия не существует.";
        if (numberEvent == -1)
            message = "Вы ввели некорректный номер мероприятия.";
        if (events.containsKey(numberEvent))
             message = events.get(numberEvent);
        botResponse.setMessage(message);
    }

    private void choosePeriod(String botCommand, BotResponse botResponse) {
        Calendar calendar = Calendar.getInstance();
        String dateFrom = null;
        String dateTo = null;
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dateMonth = calendar.get(Calendar.DATE);
        switch (botCommand) {
            case "today" -> {
                String currentDate = formatDate(calendar);
                dateFrom = currentDate;
                dateTo = currentDate;
            }
            case "tomorrow" -> {
                dateFrom = formatDate(calendar);
                calendar.add(Calendar.DATE, 1);
                dateTo = formatDate(calendar);
            }
            case "thisWeek" -> {
                String currentDate = formatDate(calendar);
                dateFrom = currentDate;
                if (dateWeek == 1)
                    dateTo = currentDate;
                else {
                    calendar.add(Calendar.DATE, 8 - dateWeek);
                    dateTo = formatDate(calendar);
                }
            }
            case "nextWeek" -> {
                if (dateWeek == 1)
                    calendar.add(Calendar.DATE, 1);
                else
                    calendar.add(Calendar.DATE, 9 - dateWeek);
                dateFrom = formatDate(calendar);
                calendar.add(Calendar.DATE, 6);
                dateTo = formatDate(calendar);
            }
            case "thisMonth" -> {
                String currentDate = formatDate(calendar);
                int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
                dateFrom = currentDate;
                if (dateMonth == lastDayMonth)
                    dateTo = currentDate;
                else {
                    calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
                    dateTo = formatDate(calendar);
                }
            }
            case "nextMonth" -> {
                int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
                if (dateMonth == lastDayMonth)
                    calendar.add(Calendar.DATE, 1);
                else
                    calendar.add(Calendar.DATE, lastDayMonth - dateMonth + 1);
                dateFrom = formatDate(calendar);
                lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
                dateMonth = calendar.get(Calendar.DATE);
                calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
                dateTo = formatDate(calendar);
            }
            default -> other(botResponse);
        }
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
        Parsing parsing = new Parsing();
        parsing.parsing(botResponse);
    }

    private String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.DATE));
        String year = Integer.toString(calendar.get(Calendar.DATE));
        return date + '.' + month + '.' + year;
    }

    private void other(BotResponse botResponse) {
        initDictionary();
        botResponse.setMessage("Введённой команды не существует, вы можете выполнить команду /help, чтобы узнать список доступных команд.");
    }

    private void initDictionary(){
        if (events.size() == 0){
            events.put(1, "Информация первого мероприятия.");
            events.put(2, "Информация второго мероприятия.");
            events.put(3, "Информация третьего мероприятия.");
        }
    }
}
