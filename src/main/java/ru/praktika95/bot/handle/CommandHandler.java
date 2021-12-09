package ru.praktika95.bot.handle;

import org.quartz.SchedulerException;
import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.helpers.CreateDate;
import ru.praktika95.bot.handle.helpers.SetDataForResponse;
import ru.praktika95.bot.handle.parsing.Parsing;
import ru.praktika95.bot.handle.parsing.ParsingConstants;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.chService.RandomService;
import ru.praktika95.bot.handle.services.dbService.DataBaseWorkService;
import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;
import ru.praktika95.bot.handle.services.chService.StringFormatService;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;
import ru.praktika95.bot.quartz.QuartzJobScheduler;

import java.util.*;


public class CommandHandler {
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
                    case "show" -> CreateDate.date(response, typeButtons);
                    case "help" -> help(response);
                    case "showMyEvents" -> myEvents(response, false);
                }
            }
            case "date" -> {
                response.setNullEvents();
                switch (botCommand) {
                    case "today" -> CreateDate.today(response, typeButtons);
                    case "tomorrow" -> CreateDate.tomorrow(response, typeButtons);
                    case "thisWeek" -> CreateDate.thisWeek(response, typeButtons);
                    case "nextWeek" -> CreateDate.nextWeek(response, typeButtons);
                    case "thisMonth" -> CreateDate.thisMonth(response, typeButtons);
                    case "nextMonth" -> CreateDate.nextMonth(response, typeButtons);
                }
            }
            case "category" -> {
                switch (botCommand) {
                    case "theatre" -> {
                        response.setCategory(ParsingConstants.categoryTheatre);
                        events(response, false);
                    }
                    case "museum" -> {
                        response.setCategory(ParsingConstants.categoryMuseum);
                        events(response, false);
                    }
                    case "concert" -> {
                        response.setCategory(ParsingConstants.categoryConcert);
                        events(response, false);
                    }
                    case "allEvents" -> {
                        response.setCategory(ParsingConstants.categoryAll);
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
                    case "day" -> SetDataForResponse.setNotification(response, TimeConstants.day);
                    case "week" -> SetDataForResponse.setNotification(response, TimeConstants.week);
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
                if ("cancel".equals(botCommand)) {
                    cancel(response);
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
        int end = start + CommandHandlerConstants.MaxEventsCount;
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
        SetDataForResponse.setButtons(typeButtons, response, response.getMySelectedEvent().getUrl());
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
        response.setText(StringFormatService.getString(CommandHandlerConstants.youCancelNotification, subscriptionDate,
                event.getEventBriefDescription(false)));
    }

    private void subscribe(Response response, String typeButtons) {
        SetDataForResponse.setMessageAndButtons(
                CommandHandlerConstants.choosePeriod, response, typeButtons, null, false);
    }

    private void buy(Response response) {
        response.setText(CommandHandlerConstants.catPolice);
        response.setPhotoFile(CommandHandlerConstants.BuyQRCodImageName);
    }

    private void showFullEvent(Response response, String typeButtons, String eventNumber) {
        int eventIndex = Integer.parseInt(eventNumber);
        response.setSelectedEvent(response.getEvents().get(eventIndex));
        SetDataForResponse.setButtons(typeButtons, response, response.getSelectedEvent().getUrl());
    }

    private void hello(Response response) {
        response.setText(CommandHandlerConstants.helloMessage);
        response.setPhotoFile(RandomService.getRandomIntegerBetweenRange(
                CommandHandlerConstants.StartImageName, CommandHandlerConstants.EndImageName));
    }

    private void help(Response response) {
        response.setText(CommandHandlerConstants.helpMessage);
        response.setPhotoFile(CommandHandlerConstants.HelpImageName);
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
        int end = start + CommandHandlerConstants.MaxEventsCount;
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

    private void other(Response response) {
        response.setText(CommandHandlerConstants.otherCommand);
    }

    public void startEventNotifier() throws SchedulerException {
        QuartzJobScheduler.startQuartzApp();
    }
}
