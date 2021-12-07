package ru.praktika95.bot.handle.helpers;

import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;
import ru.praktika95.bot.handle.services.chService.RandomService;
import ru.praktika95.bot.handle.services.dbService.DataBaseWorkService;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class setDataForResponse {
    public static void setNotification(Response response, String period) {
        Event selectedEvent = response.getSelectedEvent();
        String[] responseNotificationCapability = checkNotificationCapability(selectedEvent, period);
        if (responseNotificationCapability != null) {
            response.setPhotoFile(CommandHandlerConstants.YouShallNotPassNoticeImage);
            response.setText(String.format(CommandHandlerConstants.youCannotSetNotification,
                    responseNotificationCapability[0], responseNotificationCapability[1]));
            return;
        }
        boolean success = DataBaseWorkService.setNotificationInDateBase(period, response.getChatId(), selectedEvent);
        if (success){
            response.setSelectedEvent(selectedEvent);
            setNotificationInResponse(period, selectedEvent, response);
            response.createButtons(CommandHandlerConstants.cancelButtons,
                    response.map.get(CommandHandlerConstants.cancelButtons).toString(), false, null);
        }
        else {
            response.setPhotoFile(CommandHandlerConstants.YouShallNotPassNoticeImage);
            response.setText(CommandHandlerConstants.existNotification);
        }
    }

    public static String[] checkNotificationCapability(Event selectedEvent, String period) {
        SimpleDateFormat sdf = new SimpleDateFormat(TimeConstants.timePatternWithDash);
        String[] todayDate = sdf.format(new Date()).split(CommandHandlerConstants.dash);

        String[] eventDate = selectedEvent.getDate().split(TimeConstants.dashWithWhitespaces);
        String[] date = eventDate.length == 1
                ? eventDate[0].split(CommandHandlerConstants.dash)
                : eventDate[1].split(CommandHandlerConstants.dash);

        ZoneId z = ZoneId.of( TimeConstants.zoneId );

        ZonedDateTime today = ZonedDateTime.of(Integer.parseInt(todayDate[0]), Integer.parseInt(todayDate[1]),
                Integer.parseInt(todayDate[2]), 0, 0, 0, 0, z);
        ZonedDateTime eventD = ZonedDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), 0, 0, 0, 0, z);

        long days = ChronoUnit.DAYS.between(today , eventD);

        if (TimeConstants.day.equals(period)) {
            return days > 1
                    ? null
                    : new String[] {days + CommandHandlerConstants.whitespaces +
                    getDayAddition(Math.abs(days)), TimeConstants.day};
        }
        else {
            return days > 7
                    ? null
                    : new String[] {days + CommandHandlerConstants.whitespaces +
                    getDayAddition(Math.abs(days)), TimeConstants.day};
        }
    }

    public static void setNotificationInResponse(String period, Event selectedEvent, Response response) {
        String notificationText = selectedEvent.getEventNotification(period);
        response.setPhotoFile(RandomService.getRandomIntegerBetweenRange(CommandHandlerConstants.TelegramIconImageNameType1,
                CommandHandlerConstants.TelegramIconImageNameType2));
        response.setText(notificationText);
    }

    public static LinkedList<Response> createEvents(
            BotRequest botRequest, Response response, boolean doNext, List<Event> events, Boolean isMyEvents) {
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

    public static LinkedList<Response> createEvents(BotRequest botRequest, Response response, boolean doNext,
                                             List<Event> events, int start, int end, int delta, boolean needToFormat) {
        LinkedList<Response> responses = new LinkedList<>();
        String message;
        if (start == end || end == 0){
            response.setText(CommandHandlerConstants.noEvents);
            responses.add(response);
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
        }
        return responses;
    }
    public static void setMessageAndButtons(String message, Response response, String typeButtons,
                                     String url, boolean withPhoto) {
        response.setText(message);
        if (withPhoto)
            response.setPhotoFile(RandomService.getRandomIntegerBetweenRange(CommandHandlerConstants.StartImageName,
                    CommandHandlerConstants.EndImageName));
        setButtons(typeButtons, response, url);
    }

    public static void setButtons(String typeButtons, Response response, String url) {
        int status = response.map.get(typeButtons);
        response.createButtons(getKey(++status, response.map), null, false, url);
    }

    private static String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static String getDayAddition(long days) {

        if (days % 100 / 10 == 1) {
            return "дней";
        }

        return switch ((int) (days % 10)) {
            case 1 -> "день";
            case 2, 3, 4 -> "дня";
            default -> "дней";
        };
    }
}
