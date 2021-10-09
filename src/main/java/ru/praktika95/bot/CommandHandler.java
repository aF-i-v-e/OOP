package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class CommandHandler {
    private String[] getCommandAndEventNumber(String inputText) {
        String[] commandAndArgument = inputText.split(" ");
        String botCommand = commandAndArgument[0];
        int numberEvent = commandAndArgument.length > 1 ? Integer.parseInt(commandAndArgument[1]) : -1;
        return new String[]{botCommand, Integer.toString(numberEvent)};
    }

    public BotResponse commandHandler(String typeButtons, String botCommand) {
//        String[] commandAndEventNumber = getCommandAndEventNumber(botCommand);
//        int numberEvent = Integer.parseInt(commandAndEventNumber[1]);
        BotResponse botResponse = new BotResponse();
        switch(typeButtons) {
            case  "category" -> {
                switch (botCommand) {
                    case "theatre" -> theatre(botResponse);
                    case "movie" -> movie(botResponse);
                    case "concert" -> concert(botResponse);
                    case "allEvents" -> allEvents(botResponse);
                }
            }
//            case  "events" -> events(botResponse);
            default -> other(botResponse);
        }
        return botResponse;
    }

    private void exit(BotResponse botResponse) {
        botResponse.setStringMessage("Вы завершили работу с EkbEventsBot. Чтобы начать работу с ботом нажмите\n/start");
        botResponse.setSendPhoto(1024);
    }

    private void help(BotResponse botResponse) {
        botResponse.setStringMessage("О работе с данным ботом:\nПри вызове команды /show Вам будет предложено 6 мероприятий.\nЧтобы посмотреть больше мероприятий нажмите кнопку \"Показать ещё\".\nЕсли Вас заинтересовало мероприятие, используйте команду /choose № мероприятия.\nДля завершения работы с ботом используйте команду /exit");
        botResponse.setSendPhoto(911);
    }

    public static int getRandomIntegerBetweenRange(int min, int max){
        int x = (int)(Math.random()*((max - min) + 1)) + min;
        return x;
    }

    private void hello(BotResponse botResponse) {
        botResponse.setStringMessage("Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.\nДля того, чтобы узнать больше о работе с данным ботом используйте /help \nДля того, чтобы посмотреть доступные мероприятия используйте /show .");
        botResponse.setSendPhoto(getRandomIntegerBetweenRange(1,5));
    }

    private void show(BotResponse botResponse) {
        ParsingBotResponse(botResponse);
        String events = formEventsInfo(0,6, botResponse);
        botResponse.setStringMessage(events);
        botResponse.setSendPhoto(6);
    }

    private String formEventsInfo(int start, int end, BotResponse botResponse){
        String events="";
        for(int i = start; i < end; i++) {
            Event event = botResponse.getEvents()[i];
            events+="\n" + (i + 1) + ". "+"Мероприятие: " + event.getName() + "\nДата: " + event.getDateTime();
            if (i != end - 1)
                events +="\n \r";
        }
        return events;
    }

    private void choose(int numberEvent, BotResponse botResponse) {
        String message = "Такого мероприятия не существует.";
        if (numberEvent == -1)
            message = "Вы ввели некорректный номер мероприятия.";
        ParsingBotResponse(botResponse);
        if (numberEvent > 0 && botResponse.getEvents().length >= numberEvent)
            botResponse.setSelectedEvent(botResponse.getEvents()[numberEvent - 1]);
        else
            botResponse.setStringMessage(message);
    }

    private void theatre(BotResponse botResponse) {
        int status = 3;
        botResponse.setStringMessage("Мероприятия");
        botResponse.setButtons(createButtons(status++));
    }

    private void movie(BotResponse botResponse) {

    }

    private void concert(BotResponse botResponse) {

    }

    private void allEvents(BotResponse botResponse) {

    }

    private void today(BotResponse botResponse) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = formatDate(calendar);
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(currentDate);
        datePeriod.setDateTo(currentDate);
        botResponse.setPeriod(datePeriod);
    }

    private void tomorrow(BotResponse botResponse) {
        Calendar calendar = Calendar.getInstance();
        String dateFrom = formatDate(calendar);
        calendar.add(Calendar.DATE, 1);
        String dateTo = formatDate(calendar);
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
    }

    private void thisWeek(BotResponse botResponse) {
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
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(currentDate);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
    }

    private void nextWeek(BotResponse botResponse) {
        Calendar calendar = Calendar.getInstance();
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dateWeek == 1)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, 9 - dateWeek);
        String dateFrom = formatDate(calendar);
        calendar.add(Calendar.DATE, 6);
        String dateTo = formatDate(calendar);
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
    }

    private void thisMonth(BotResponse botResponse) {
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
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(currentDate);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
    }

    private void nextMonth(BotResponse botResponse) {
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
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        botResponse.setPeriod(datePeriod);
    }

    private InlineKeyboardMarkup createButtons(int status){
        Buttons buttons = new Buttons();
        try {
            return buttons.createButtons(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    private void ParsingBotResponse(BotResponse botResponse){
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
        botResponse.setStringMessage("Введённой команды не существует, вы можете выполнить команду /help, чтобы узнать как пользоваться ботом.");
    }
}
