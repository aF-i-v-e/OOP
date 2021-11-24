package ru.praktika95.bot;

import java.util.*;

public class CommandHandler {

    private final String dateText = "Выберите категорию мероприятия, которое состоится";
    final int HelpImageName = 911;
    final int StartImageName = 1;
    final int EndImageName = 5;
    final int MaxEventsCount = 3;//было 6
    final int BuyQRCodImageName = 102;
    final int TelegramIconImageNameType1 = 841;//in unicode t has number 84 and image has the first type => 841
    final int TelegramIconImageNameType2 = 842;

    public void commandHandler(String basicCommand, Response response){
        response.setNullEvents();
        switch (basicCommand) {
            case "help" -> help(response);
            case "start" ->  hello(response);
            default -> other(response);
        }
    }

    public void commandHandler(BotRequest botRequest, Response response) {
        String typeButtons = botRequest.getTypeButtons();
        String botCommand = botRequest.getBotCommand();
        String selectedEventNumber = botRequest.getSelectedEvent();
        switch (typeButtons) {
            case "main" -> {
                response.setNullEvents();
                switch (botCommand) {
                    case "show" -> date(response, typeButtons);
                    case "help" -> help(response);
                }
            }
            case "date" -> {
                response.setNullEvents();
                switch (botCommand) {
                    case "today" -> today(response, typeButtons);
                    case "tomorrow" -> tomorrow(response, typeButtons);
                    case "thisWeek" -> thisWeek(response, typeButtons);
                    case "nextWeek" -> nextWeek(response, typeButtons);
                    case "thisMonth" -> thisMonth(response, typeButtons);
                    case "nextMonth" -> nextMonth(response, typeButtons);
                }
            }
            case "category" -> {
                switch (botCommand) {
                    case "theatre" -> {
                        response.setCategory("3009");
                        events(response, false);
                    }
                    case "museum" -> {
                        response.setCategory("4093");
                        events(response, false);
                    }
                    case "concert" -> {
                        response.setCategory("3000");
                        events(response, false);
                    }
                    case "allEvents" -> {
                        response.setCategory("0");
                        events(response, false);
                    }
                }
            }
            case "events" -> {
                switch (botCommand) {
                    case "next" -> events(response, true);
                    case "event" -> showFullEvent(response, typeButtons, selectedEventNumber);
                }
            }
            case "event" -> {
                switch (botCommand) {
                    case "subscribe" -> subscribe(response, typeButtons);
                    case "buy" -> buy(response);
                }
            }
            case "period" -> {
                switch (botCommand) {
                    case "day" -> day(response);
                    case "week" -> week(response);
                }
            }
            default -> other(response);
        }
    }

    private void day(Response response) {
        String notificationText = eventNotification(response, "день");
        response.setText(notificationText);
    }

    private void week(Response response) {
        String notificationText = eventNotification(response, "неделю");
        response.setText(notificationText);
    }

    private String eventNotification(Response response, String period) {
        response.setPhotoFile(getRandomIntegerBetweenRange(TelegramIconImageNameType1, TelegramIconImageNameType2));
        String eventName = "Вы выбрали: \""  + response.getSelectedEvent().getName() + "\"";
        String eventDate = "\nОно состоится: " + response.getSelectedEvent().getDateTime();
        String notification = "\nEkbEventBot оповестит Вас за " + period + " о мероприятии, которое Вы выбрали";
        String resultText = eventName + eventDate + notification;
        return resultText;
    }

    private void subscribe(Response response, String typeButtons) {
        setMessageAndButtons("Выберите период, за который Вы хотите, чтобы бот Вас оповестил о мероприятии:", response, typeButtons, null, false);
    }

    private void buy(Response response) {
        response.setText("\nВас посетила полиция котиков!\nНа этот раз без штрафа, но впредь будьте аккуратнее!");
        response.setPhotoFile(BuyQRCodImageName);
    }

    private void showFullEvent(Response response, String typeButtons, String eventNumber) {
        int eventIndex = Integer.parseInt(eventNumber);
        response.setSelectedEvent(response.getEvents().get(eventIndex));
        setButtons(typeButtons, response, response.getSelectedEvent().getUrl());
    }

    public LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean isNext) {
        LinkedList<Response> responses = new LinkedList<>();
        String message;
        int start = response.getStartEventNumber();
        int end = response.getEndEventNumber();
        if (start == end || end == 0){
            response.setText("Больше мероприятий по выбранным параметрам нет");
            responses.add(response);
            return responses;
        }
        else{
            for (int i = start; i < end; i++) {
                Event event = response.getEvents().get(i);
                message = "\n✧ Мероприятие: " + event.getName() + "\n✧ Дата: " + FormatDate.userFormatDate(event.getDate()) + " " + event.getTime();
                int status = response.map.get(botRequest.getTypeButtons());
                response.setText(message);
                response.setPhotoFile(event.getPhoto());
                boolean isEnd = i == end - 1;
                if (!isNext)
                    ++status;
                response.createButtons(getKey(status, response.map), Integer.toString(i), isEnd, null);
                Response helpEvent = new Response(response);
                responses.add(helpEvent);
            }
            response.setStartEventNumber(end);
            return responses;
        }
    }

    private void hello(Response response) {
        response.setText("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.\nДля того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"\nДля того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку \"Мероприятия\".");
        response.setPhotoFile(getRandomIntegerBetweenRange(StartImageName, EndImageName));
    }

    private void help(Response response) {
        response.setText("О работе с данным ботом:\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 6 мероприятий.\nКогда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\nПри подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
        response.setPhotoFile(HelpImageName);
    }

    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    private void date(Response response, String typeButtons){
        setMessageAndButtons("Выберите дату", response, typeButtons, null, true);
    }

    private void today(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = formatDate(calendar);
        setMessageAndButtons(dateText + " сегодня:",
                createDatePeriod(response, currentDate, currentDate), typeButtons, null, true);
    }

    private void tomorrow(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String tomorrow = formatDate(calendar);
        setMessageAndButtons(dateText + " завтра:",
                createDatePeriod(response, tomorrow, tomorrow), typeButtons, null, true);
    }

    private void thisWeek(Response response, String typeButtons) {
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
                createDatePeriod(response, currentDate, dateTo), typeButtons, null, true);
    }

    private void nextWeek(Response response, String typeButtons) {
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
                createDatePeriod(response, dateFrom, dateTo), typeButtons, null, true);
    }

    private void thisMonth(Response response, String typeButtons) {
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
                createDatePeriod(response, currentDate, dateTo), typeButtons, null, true);
    }

    private void nextMonth(Response response, String typeButtons) {
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
                createDatePeriod(response, dateFrom, dateTo), typeButtons, null, true);
    }

    private String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        return date + '.' + month + '.' + year;
    }

    private Response createDatePeriod(Response response, String dateFrom, String dateTo){
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        response.setPeriod(datePeriod);
        return response;
    }

    private void events(Response response, boolean isNext) {
        int start;
        int countEvent = response.getEvents().size();
        if (!isNext){
            ParsingBotResponse(response);
            start = countEvent;
        }
        else
            start = response.getStartEventNumber();
        int end = start + MaxEventsCount;
        countEvent = response.getEvents().size();
        if (end > countEvent)
            end = countEvent;
        response.setStartEventNumber(start);
        response.setEndEventNumber(end);
    }

    private void ParsingBotResponse(Response response){
        Parsing parsing = new Parsing();
        parsing.parsing(response);
    }

    private void setMessageAndButtons(String message, Response response, String typeButtons, String url, boolean withPhoto) {
        response.setText(message);
        if (withPhoto)
            response.setPhotoFile(getRandomIntegerBetweenRange(StartImageName, EndImageName));
        setButtons(typeButtons, response, url);
    }

    private void setButtons(String typeButtons, Response response, String url) {
        int status = response.map.get(typeButtons);
        response.createButtons(getKey(++status, response.map), null, false, url);
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void other(Response response) {
        response.setText("Введённой команды не существует, вы можете выполнить команду /start, чтобы начать работу с ботом.");
    }
}
