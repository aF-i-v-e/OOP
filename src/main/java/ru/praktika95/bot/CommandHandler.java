package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandHandler {
//    private final Map<String, Handler> commandsHandlers = Map.of(
//            "show", this::date
//    );

    private final String dateText = "Выберите категорию мероприятия, которое состоится";

    public void commandHandler(String basicCommand, BotResponse botResponse){
        switch (basicCommand) {
            case "help" -> help(botResponse);
            case "start" ->  hello(botResponse);
            default -> other(botResponse);
        }
    }

    public void commandHandler(String typeButtons, String botCommand, BotResponse botResponse) {
        switch (typeButtons) {
            case "main" -> {
//                commandsHandlers.get(botCommand).handle(botResponse, typeButtons);
                switch (botCommand) {
                    case "show" -> date(botResponse, typeButtons);
                    case "help" -> help(botResponse);
                }
            }
            case "date" -> {
                switch (botCommand) {
                    case "today" -> today(botResponse, typeButtons);
                    case "tomorrow" -> tomorrow(botResponse, typeButtons);
                    case "thisWeek" -> thisWeek(botResponse, typeButtons);
                    case "nextWeek" -> nextWeek(botResponse, typeButtons);
                    case "thisMonth" -> thisMonth(botResponse, typeButtons);
                    case "nextMonth" -> nextMonth(botResponse, typeButtons);
                }
            }
            case "category" -> {
                switch (botCommand) {
                    case "theatre" -> {
                        botResponse.setCategory("3009");
                        events(botResponse, typeButtons);
                    }
                    case "museum" -> {
                        botResponse.setCategory("4093");
                        events(botResponse, typeButtons);
                    }
                    case "concert" -> {
                        botResponse.setCategory("3000");
                        events(botResponse, typeButtons);
                    }
                    case "allEvents" -> {
                        botResponse.setCategory("0");
                        events(botResponse, typeButtons);
                    }
                }
            }
            case "events" -> events(botResponse, typeButtons);
            default -> other(botResponse);
        }
    }

    private void exit(BotResponse botResponse) {
        botResponse.setMessage("Вы завершили работу с EkbEventsBot. Чтобы начать работу с ботом нажмите\n/start");
        botResponse.setSendPhoto(1024);
    }

    private void hello(BotResponse botResponse) {
        botResponse.setMessage("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.\nДля того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"\nДля того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку \"Мероприятия\".");
        botResponse.setSendPhoto(getRandomIntegerBetweenRange(1, 5));
    }

    private void help(BotResponse botResponse) {
        botResponse.setMessage("О работе с данным ботом:\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 6 мероприятий.\nКогда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\nПри подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
        botResponse.setSendPhoto(911);
    }

    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    private void choose(int numberEvent, BotResponse botResponse) {
        String message = "Такого мероприятия не существует.";
        if (numberEvent == -1)
            message = "Вы ввели некорректный номер мероприятия.";
//        ParsingBotResponse(botResponse);
        if (numberEvent > 0 && botResponse.getEvents().length >= numberEvent)
            botResponse.setSelectedEvent(botResponse.getEvents()[numberEvent - 1]);
        else
            botResponse.setMessage(message);
    }

    private void date(BotResponse botResponse, String typeButtons){
        setMessageAndButtons("Выберите дату", botResponse, typeButtons);
    }

    private void today(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = formatDate(calendar);
        setMessageAndButtons(dateText + " сегодня:",
                createDatePeriod(botResponse, currentDate, currentDate), typeButtons);
    }

    private void tomorrow(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        String dateFrom = formatDate(calendar);
        calendar.add(Calendar.DATE, 1);
        String dateTo = formatDate(calendar);
        setMessageAndButtons(dateText + " завтра:",
                createDatePeriod(botResponse, dateFrom, dateTo), typeButtons);
    }

    private void thisWeek(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = formatDate(calendar);
        String dateTo;
        if (dateWeek == 1)
            dateTo = currentDate;
        else {
            calendar.add(Calendar.DATE, 8 - dateWeek);
            dateTo = formatDate(calendar);
        }
        setMessageAndButtons(dateText + " на этой неделе:",
                createDatePeriod(botResponse, currentDate, dateTo), typeButtons);
    }

    private void nextWeek(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dateWeek == 1)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, 9 - dateWeek);
        String dateFrom = formatDate(calendar);
        calendar.add(Calendar.DATE, 6);
        String dateTo = formatDate(calendar);
        setMessageAndButtons(dateText + " на следующей неделе:",
                createDatePeriod(botResponse, dateFrom, dateTo), typeButtons);
    }

    private void thisMonth(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateMonth = calendar.get(Calendar.DATE);
        String currentDate = formatDate(calendar);
        int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        String dateTo;
        if (dateMonth == lastDayMonth)
            dateTo = currentDate;
        else {
            calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
            dateTo = formatDate(calendar);
        }
        setMessageAndButtons(dateText + " в этом месяце:",
                createDatePeriod(botResponse, currentDate, dateTo), typeButtons);
    }

    private void nextMonth(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateMonth = calendar.get(Calendar.DATE);
        int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        if (dateMonth == lastDayMonth)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, lastDayMonth - dateMonth + 1);
        String dateFrom = formatDate(calendar);
        lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        dateMonth = calendar.get(Calendar.DATE);
        calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
        String dateTo = formatDate(calendar);
        setMessageAndButtons( dateText + " в следующем месяце:",
                createDatePeriod(botResponse, dateFrom, dateTo), typeButtons);
    }

    private String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.DATE));
        String year = Integer.toString(calendar.get(Calendar.DATE));
        return date + '.' + month + '.' + year;
    }

    private BotResponse createDatePeriod(BotResponse botResponse, String dateFrom, String dateTo){
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
        return botResponse;
    }

    private void events(BotResponse botResponse, String typeButtons) {
        ParsingBotResponse(botResponse);
        int start = botResponse.getStartEvent();
        int end = start + 6;
        int countEvent = botResponse.getCountEvent();
        if (end > countEvent)
            end = countEvent % 6;
        createEvents(start, end, botResponse, typeButtons);
    }

    private void createEvents(int start, int end, BotResponse botResponse, String typeButtons) {
        String message;
        for (int i = start; i < end; i++) {
            Event event = botResponse.getEvents()[i];
            message = "\n" + (i + 1) + ". " + "Мероприятие: " + event.getName() + "\nДата: " + event.getDateTime();
            if (i != end - 1)
                message += "\n \r";
            int status = botResponse.map.get(typeButtons);
            botResponse.setMessage(message);
            botResponse.setSendPhoto(event.getPhoto());
            botResponse.setButtons(createButtons(++status, botResponse.map, Integer.toString(i), event.getUrl()));
        }
    }

    private void ParsingBotResponse(BotResponse botResponse){
        Parsing parsing = new Parsing();
        parsing.parsing(botResponse);
    }

    private void setMessageAndButtons(String message, BotResponse botResponse, String typeButtons){
        int status = botResponse.map.get(typeButtons);
        botResponse.setMessage(message);
        botResponse.setSendPhoto(getRandomIntegerBetweenRange(1, 5));
        botResponse.setButtons(createButtons(++status, botResponse.map, null, null));
    }

    private InlineKeyboardMarkup createButtons(int status, Map<String, Integer> map, String number, String url) {
        Buttons buttons = new Buttons();
        try {
            return buttons.createButtons(getKey(status, map), number, url);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    private InlineKeyboardMarkup createButtonsNextEvents(String number, String url) {
        Buttons buttons = new Buttons();
        try {
            return buttons.createButtons("nextEvents", number, url);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void other(BotResponse botResponse) {
        botResponse.setMessage("Введённой команды не существует, вы можете выполнить команду /start, чтобы начать работу с ботом.");
    }
}
