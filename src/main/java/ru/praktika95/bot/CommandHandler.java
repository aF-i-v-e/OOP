package ru.praktika95.bot;

import ru.praktika95.bot.hibernate.*;

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
    final int ExistNoticeImage = 409;

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
        UsersCRUD usersCRUD = new UsersCRUD();
        List<User> usersList = usersCRUD.getByChatId(userChatId);
        List<Event> events = restoreEvent(usersList);
        response.setMyEventsList(events);
    }

    public void showMyFullEvent(Response response, String typeButtons, String selectedMyEventNumber) {
        int eventIndex = Integer.parseInt(selectedMyEventNumber);
        response.setMySelectedEvent(response.getMyEventsList().get(eventIndex));
        setButtons(typeButtons, response, response.getMySelectedEvent().getUrl());
    }

    private LinkedList<Event> restoreEvent(List<User> usersList) {
        LinkedList<Event> events = new LinkedList<>();
        for (User user : usersList){
            Event event = new Event(user.getEventPhoto(),user.getEventName(), user.getEventDate(), user.getEventTime(), user.getEventPlace(), user.getEventPrice(), user.getEventUrl());
            event.setDateNotice(user.getEventDateNotice());
            event.setIdBD(user.getId());
            events.add(event);
        }
        return events;
    }

    private void cancel(Response response){
        Event eventToDelete = response.getSelectedEvent();
        UsersCRUD usersCRUD = new UsersCRUD();
        eventToDelete.setIdBD(usersCRUD.getLastId());
        deleteEventAndSetMessage(response, eventToDelete);
    }

    private void cancelSubscribe(Response response) {
        Event eventToDelete = response.getMySelectedEvent();
        deleteEventAndSetMessage(response, eventToDelete);
    }

    private void deleteEventAndSetMessage(Response response, Event event) {
        String subscriptionDate = deleteFromDB(event);
        response.setText("Вы отменили оповещение на " + subscriptionDate + "." + event.getEventBriefDescription(false));
    }

    private String deleteFromDB(Event event) {
        UsersCRUD usersCRUD = new UsersCRUD();
        User userToDelete = usersCRUD.getById(event.getIdBD());
        String subscriptionDate = userToDelete.getEventDateNotice();
        usersCRUD.delete(userToDelete);
        return  subscriptionDate;
    }

    private void setNotification(Response response, String period) {
        Event selectedEvent = response.getSelectedEvent();
        Boolean success = setNotificationInDateBase(period, response.getChatId(), selectedEvent);
        if (success){
            setNotificationInResponse(period, selectedEvent, response);
            response.createButtons("cancel", "8", false, null);
        }
        else {
            response.setPhotoFile(ExistNoticeImage);
            response.setText("Вы уже подписаны на это мероприятие!");
        }
    }

    private void setNotificationInResponse(String period, Event selectedEvent, Response response) {
        String notificationText = selectedEvent.getEventNotification(period);
        response.setPhotoFile(getRandomIntegerBetweenRange(TelegramIconImageNameType1, TelegramIconImageNameType2));
        response.setText(notificationText);
    }

    private boolean setNotificationInDateBase(String period, String chatId, Event selectedEvent) {
        UsersCRUD usersCRUD = new UsersCRUD();
        User user = new User(chatId, selectedEvent);
        System.out.println(selectedEvent.getDate());
        String[] dateTwo = selectedEvent.getDate().split(" - ");
        String[] date = dateTwo.length == 1 ? dateTwo[0].split("-") : dateTwo[1].split("-");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
        int delta = period == "день" ? -1 : -7;
        calendar.add(Calendar.DATE, delta);
        user.setEventDateNotice(formatDate(calendar));
        if (!usersCRUD.existNote(user)) {
            usersCRUD.save(user);
            return true;
            //нужно получить id этого занесенного юзера и назначить этот id юзеру - это нужно для того, чтобы потом в cancel удалить по id
        }
        return false;
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

    public LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean doNext, List<Event> events, Boolean isMyEvents) {
        int start;
        int end;
        int delta = 0; //сдвиг кнопочек в map
        LinkedList<Response> result;
        if (isMyEvents){
            start = response.getMyStartEventNumber();
            end = response.getMyEndEventNumber();
            if (!doNext) //если пользовательно не выбрал показать еще, то сдвиг в мапе равен 5 между нужными кнопочками
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

    public LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean doNext, List<Event> events, int start, int end, int delta, boolean needToFormat) {
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
