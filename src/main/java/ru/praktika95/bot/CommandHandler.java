package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

public class CommandHandler {

    private final String dateText = "Выберите категорию мероприятия, которое состоится";
    final int ExitImageNumber = 1024;
    final int HelpImageNumber = 911;
    final int StartImageNumber = 1;
    final int EndImageNumber = 5;
    final int MaxEventsCount = 3;//было 6
    final int BuyQRCod = 102;

    public void commandHandler(String basicCommand, BotResponse botResponse){
        botResponse.setNullEvents();
        switch (basicCommand) {
            case "help" -> help(botResponse);
            case "start" ->  hello(botResponse);
            default -> other(botResponse);
        }
    }

    public void commandHandler(BotRequest botRequest, BotResponse botResponse) {
        String typeButtons = botRequest.getTypeButtons();
        String botCommand = botRequest.getBotCommand();
        String selectedEventNumber = botRequest.getSelectedEvent();
        switch (typeButtons) {
            case "main" -> {
                botResponse.setNullEvents();
                switch (botCommand) {
                    case "show" -> date(botResponse, typeButtons);
                    case "help" -> help(botResponse);
                }
            }
            case "date" -> {
                botResponse.setNullEvents();
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
                        events(botResponse, false);
                    }
                    case "museum" -> {
                        botResponse.setCategory("4093");
                        events(botResponse, false);
                    }
                    case "concert" -> {
                        botResponse.setCategory("3000");
                        events(botResponse, false);
                    }
                    case "allEvents" -> {
                        botResponse.setCategory("0");
                        events(botResponse, false);
                    }
                }
            }
            case "events" -> {
                switch (botCommand) {
                    case "next" -> events(botResponse, true);
                    case "event" -> showFullEvent(botResponse, selectedEventNumber);
                }
            }
            case "period" -> {
                switch (botCommand) {
                    case "subscribe" -> subscribe();
                    case "buy" -> buy(botResponse);
                }
            }
            default -> other(botResponse);
        }
    }

    public void subscribe() {

    }

    public void buy(BotResponse botResponse) {
        botResponse.setMessage("\nВас посетила полиция котиков!\nНа это раз без штрафа, но впредь будьте аккуратнее!");
        botResponse.setSendPhoto(BuyQRCod);
    }

    public void showFullEvent(BotResponse botResponse, String eventNumber) {
        int eventIndex = Integer.parseInt(eventNumber);
        botResponse.setSelectedEvent(botResponse.getEvents().get(eventIndex));
        setButtons("event", botResponse, botResponse.getSelectedEvent().getUrl());
    }

    public LinkedList<BotResponse> createEvents(BotRequest botRequest, BotResponse botResponse, boolean isNext) {
        LinkedList<BotResponse> botResponses = new LinkedList<>();
        String message;
        int start = botResponse.getStartEvent();
        int end = botResponse.getEndEvent();
        if (start == end || end == 0){
            botResponse.setMessage("Больше мероприятий по выбранным параметрам нет");
            botResponses.add(botResponse);
            return botResponses;
        }
        else{
            for (int i = start; i < end; i++) {
                Event event = botResponse.getEvents().get(i);
                message = "\n✧ Мероприятие: " + event.getName() + "\n✧ Дата: " + event.getDateTime();
                int status = botResponse.map.get(botRequest.getTypeButtons());
                botResponse.setMessage(message);
                botResponse.setSendPhoto(event.getPhoto());
                boolean isEnd = i == end - 1;
                if (!isNext)
                    ++status;
                botResponse.createButtons(getKey(status, botResponse.map), Integer.toString(i), isEnd, null);
                BotResponse helpBot = new BotResponse(botResponse);
                botResponses.add(helpBot);
            }
            botResponse.setStartEvent(end);
            return botResponses;
        }
    }

    private void exit(BotResponse botResponse) {
        botResponse.setMessage("Вы завершили работу с EkbEventsBot. Чтобы начать работу с ботом нажмите\n/start");
        botResponse.setSendPhoto(ExitImageNumber);
    }

    private void hello(BotResponse botResponse) {
        botResponse.setMessage("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.\nДля того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"\nДля того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку \"Мероприятия\".");
        botResponse.setSendPhoto(getRandomIntegerBetweenRange(StartImageNumber, EndImageNumber));
    }

    private void help(BotResponse botResponse) {
        botResponse.setMessage("О работе с данным ботом:\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 6 мероприятий.\nКогда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\nПри подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
        botResponse.setSendPhoto(HelpImageNumber);
    }

    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

//    private void choose(int numberEvent, BotResponse botResponse) {
//        String message = "Такого мероприятия не существует.";
//        if (numberEvent == -1)
//            message = "Вы ввели некорректный номер мероприятия.";
//        ParsingBotResponse(botResponse);
//        if (numberEvent > 0 && botResponse.getEvents().length >= numberEvent)
//            botResponse.setSelectedEvent(botResponse.getEvents()[numberEvent - 1]);
//        else
//            botResponse.setMessage(message);
//    }

    private void date(BotResponse botResponse, String typeButtons){
        setMessageAndButtons("Выберите дату", botResponse, typeButtons, null);
    }

    private void today(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = formatDate(calendar);
        setMessageAndButtons(dateText + " сегодня:",
                createDatePeriod(botResponse, currentDate, currentDate), typeButtons, null);
    }

    private void tomorrow(BotResponse botResponse, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String tomorrow = formatDate(calendar);
        setMessageAndButtons(dateText + " завтра:",
                createDatePeriod(botResponse, tomorrow, tomorrow), typeButtons, null);
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
                createDatePeriod(botResponse, currentDate, dateTo), typeButtons, null);
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
                createDatePeriod(botResponse, dateFrom, dateTo), typeButtons, null);
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
                createDatePeriod(botResponse, currentDate, dateTo), typeButtons, null);
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
                createDatePeriod(botResponse, dateFrom, dateTo), typeButtons, null);
    }

    private String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        return date + '.' + month + '.' + year;
    }

    private BotResponse createDatePeriod(BotResponse botResponse, String dateFrom, String dateTo){
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
        return botResponse;
    }

    private void events(BotResponse botResponse, boolean isNext) {
        int start;
        int countEvent = botResponse.getEvents().size();
        if (!isNext){
            ParsingBotResponse(botResponse);
            start = countEvent;
        }
        else
            start = botResponse.getStartEvent();
        int end = start + MaxEventsCount;
        countEvent = botResponse.getEvents().size();
        if (end > countEvent)
            end = countEvent;
        botResponse.setStartEvent(start);
        botResponse.setEndEvent(end);
    }

    private void ParsingBotResponse(BotResponse botResponse){
        Parsing parsing = new Parsing();
        parsing.parsing(botResponse);
    }

    private void setMessageAndButtons(String message, BotResponse botResponse, String typeButtons, String url) {
        botResponse.setMessage(message);
        botResponse.setSendPhoto(getRandomIntegerBetweenRange(StartImageNumber, EndImageNumber));
        setButtons(typeButtons, botResponse, url);
    }

    private void setButtons(String typeButtons, BotResponse botResponse, String url) {
        int status = botResponse.map.get(typeButtons);
        botResponse.createButtons(getKey(++status, botResponse.map), null, false, url);
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
