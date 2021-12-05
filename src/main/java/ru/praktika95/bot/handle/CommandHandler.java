package ru.praktika95.bot.handle;

import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.parsing.Parsing;
import ru.praktika95.bot.handle.response.DatePeriod;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.DataBaseWorkService;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.praktika95.bot.handle.format.FormatDateCalendar.formatDate;


public class CommandHandler {

    private final String dateText = "Выберите категорию мероприятия, которое состоится";
    final int HelpImageName = 911;
    final int StartImageName = 1;
    final int EndImageName = 5;
    final int MaxEventsCount = 3;//было 6
    final int BuyQRCodImageName = 102;
    final int TelegramIconImageNameType1 = 841;//in unicode t has number 84 and image has the first type => 841
    final int TelegramIconImageNameType2 = 842;
    final int YouShallNotPassNoticeImage = 410;

    public void commandHandler(String basicCommand, Response response){
        response.setNullEvents();
        response.setNullMyEvents();
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
        String selectedMyEventNumber = botRequest.getMySelectedEvent();
        switch (typeButtons) {
            case "main" -> {
                response.setNullEvents();
                response.setNullMyEvents();
                switch (botCommand) {
                    case "show" -> date(response, typeButtons);
                    case "help" -> help(response);
                    case "showMyEvents" -> myEvents(response, false);
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
                    case "day" -> setNotification(response, "день");
                    case "week" -> setNotification(response, "неделю");
                }
            }
            case "myevents" -> {
                switch (botCommand) {
                    case "nextMyEvent" -> myEvents(response, true);
                    case "myevent" -> showMyFullEvent(response, typeButtons, selectedMyEventNumber);
                }
            }
            case "myevent" -> {
                switch (botCommand) {
                    case "cancelSubscribe" -> cancelSubscribe(response);
                    case "buy" -> buy(response);
                }
            }
            case "cancel" -> {
                switch (botCommand) {
                    case "cancel" -> cancel(response);
                }
            }
            default -> other(response);
        }
    }

    private void myEvents(Response response, Boolean isNextMyEvent) {
        int start;
        int countEvent = response.getMyEventsList().size();
        if (!isNextMyEvent) {
            setMyEventsList(response);
            start = countEvent;
        }
        else
            start = response.getMyStartEventNumber();
        int end = start + MaxEventsCount;
        countEvent = response.getMyEventsList().size();
        if (end > countEvent)
            end = countEvent;
        response.setMyStartEventNumber(start);
        response.setMyEndEventNumber(end);
    }

    private void setMyEventsList(Response response) {
        String userChatId = response.getChatId();
        List<Event> events = DataBaseWorkService.getEventListByChatId(userChatId);
        response.setMyEventsList(events);
    }

    public void showMyFullEvent(Response response, String typeButtons, String selectedMyEventNumber) {
        int eventIndex = Integer.parseInt(selectedMyEventNumber);
        response.setMySelectedEvent(response.getMyEventsList().get(eventIndex));
        setButtons(typeButtons, response, response.getMySelectedEvent().getUrl());
    }

    private void cancel(Response response){
        Event eventToDelete = response.getSelectedEvent();
        eventToDelete.setIdBD(DataBaseWorkService.getLastId());
        deleteEventAndSetMessage(response, eventToDelete);
    }

    private void cancelSubscribe(Response response) {
        Event eventToDelete = response.getMySelectedEvent();
        deleteEventAndSetMessage(response, eventToDelete);
    }

    private void deleteEventAndSetMessage(Response response, Event event) {
        String subscriptionDate = DataBaseWorkService.deleteByEvent(event);
        response.setText("Вы отменили оповещение на " + subscriptionDate + "." + event.getEventBriefDescription(false));
    }

    private void setNotification(Response response, String period) {
        Event selectedEvent = response.getSelectedEvent();
        String responseNotificationCapability = checkNotificationCapability(selectedEvent, period);
        if (responseNotificationCapability != null) {
            response.setPhotoFile(YouShallNotPassNoticeImage);
            String[] answer = responseNotificationCapability.split("-");
            response.setText(String.format("Мероприятие через %s дней, мы не можем уведомить Вас за %s!", answer[0], answer[1]));
            return;
        }
        boolean success = DataBaseWorkService.setNotificationInDateBase(period, response.getChatId(), selectedEvent);
        if (success){
            response.setSelectedEvent(selectedEvent);
            setNotificationInResponse(period, selectedEvent, response);
            response.createButtons("cancel", "8", false, null);
        }
        else {
            response.setPhotoFile(YouShallNotPassNoticeImage);
            response.setText("Вы уже подписаны на это мероприятие!");
        }
    }

    private String checkNotificationCapability(Event selectedEvent, String period) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] todayDate = sdf.format(new Date()).split("-");

        String[] eventDate = selectedEvent.getDate().split(" - ");
        String[] date = eventDate.length == 1 ? eventDate[0].split("-") : eventDate[1].split("-");

        ZoneId z = ZoneId.of( "UTC+5" );

        ZonedDateTime today = ZonedDateTime.of(Integer.parseInt(todayDate[0]), Integer.parseInt(todayDate[1]), Integer.parseInt(todayDate[2]), 0, 0, 0, 0, z);
        ZonedDateTime eventD = ZonedDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 0, 0, 0, 0, z);

        long days = ChronoUnit.DAYS.between(today , eventD);

        if (period == "день") {
            if (days > 1)
                return null;
            return days + "-" + "день";
        }
        else {
            if (days > 7)
                return null;
            return days + "-" + "неделю";
        }
    }

    private void setNotificationInResponse(String period, Event selectedEvent, Response response) {
        String notificationText = selectedEvent.getEventNotification(period);
        response.setPhotoFile(getRandomIntegerBetweenRange(TelegramIconImageNameType1, TelegramIconImageNameType2));
        response.setText(notificationText);
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

    public LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean doNext,
                                             List<Event> events, Boolean isMyEvents) {
        int start;
        int end;
        int delta = 0;
        LinkedList<Response> result;
        if (isMyEvents){
            start = response.getMyStartEventNumber();
            end = response.getMyEndEventNumber();
            if (!doNext)
                delta = 5;
            result = createEvents(botRequest, response, doNext, events,start, end, delta, false);
            response.setMyStartEventNumber(end);
        }
        else
        {
            start = response.getStartEventNumber();
            end = response.getEndEventNumber();
            result = createEvents(botRequest, response, doNext, events,start, end, delta, true);
            response.setStartEventNumber(end);
        }
        return result;
    }

    public LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean doNext,
                                             List<Event> events, int start, int end, int delta, boolean needToFormat) {
        LinkedList<Response> responses = new LinkedList<>();
        String message;
        if (start == end || end == 0){
            response.setText("Больше мероприятий по выбранным параметрам нет");
            responses.add(response);
            return responses;
        }
        else if (events.size() == 0) {
            response.setText("Список выбранных Вами мероприятий пуст!\nИспользуйте кнопку \"Показать мероприятия\", чтобы посмотреть доступные мероприятия.");
            responses.add(response);
            return responses;
        }
        else{
            for (int i = start; i < end; i++) {
                Event event = events.get(i);
                message = event.getEventBriefDescription(needToFormat);
                int status = response.map.get(botRequest.getTypeButtons()) + delta;
                response.setText(message);
                response.setPhotoFile(event.getPhoto());
                boolean isEnd = i == end - 1;
                if (!doNext)
                    ++status;
                response.createButtons(getKey(status, response.map), Integer.toString(i), isEnd, null);
                Response helpEvent = new Response(response);
                responses.add(helpEvent);
            }
            return responses;
        }
    }

    private void hello(Response response) {
        response.setText("Привет!\nЯ бот, которые может показать ближайшие мероприятия. " +
                "Вы можете подписаться на их уведомление и вы точно про него не забудете." +
                "\nДля того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"" +
                "\nДля того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку" +
                " \"Мероприятия\".");
        response.setPhotoFile(getRandomIntegerBetweenRange(StartImageName, EndImageName));
    }

    private void help(Response response) {
        response.setText("О работе с данным ботом:" +
                "\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, " +
                "используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 3 мероприятия." +
                "\nКогда Вы выберете конкретное мероприятие, Вы сможете сразу приобрести билеты, либо " +
                "поставить на него уведомление." +
                "\nДоступно оповещение о событии за день, за неделю, либо за оба периода сразу." +
                "\nДля отмены уведомления Вам нужно найти на главном меню кнопку \"Мои мероприятия\" " +
                "и в появившемся списке выбрать мероприятие, оповещение на которое нужно убрать." +
                "\nДля полной отмены уведомления на событие, нужно исключить все записи о данном мероприятии из списка" +
                " \"Мои мероприятия\".");
        response.setPhotoFile(HelpImageName);
    }

    private static int getRandomIntegerBetweenRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
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
