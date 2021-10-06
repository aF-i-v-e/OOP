package ru.praktika95.bot;

import java.util.*;

public class CommandHandler {

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
            case "/start"-> hello(botResponse) ;
            case "/help"-> help(botResponse);
            case "/show"-> show(botResponse);
            case "/chooseCategory"-> botResponse.setCategory(codeCategory);
            case "/choosePeriod"-> choosePeriod(botCommand, botResponse);
            case "/exit"-> exit(botResponse);
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

//        InlineKeyboardButton b1= new InlineKeyboardButton();
//        b1.setText("Кино");
//        b1.setCallbackData("Вы выбрали категорию кино");
//
//        InlineKeyboardButton b2= new InlineKeyboardButton();
//        b2.setText("Театр");
//        b2.setCallbackData("Вы выбрали категорию театр");
//
//        InlineKeyboardButton b3= new InlineKeyboardButton();
//        b3.setText("Выставка");
//        b3.setCallbackData("Вы выбрали категорию выставка");
//
//        List<InlineKeyboardButton> buttons1=new ArrayList<>();
//        buttons1.add(b1);
//        buttons1.add(b2);
//        buttons1.add(b3);
//
//        List<InlineKeyboardButton> buttons2=new ArrayList<>();
//        InlineKeyboardButton b4= new InlineKeyboardButton();
//        b4.setText("Помощь");
//        BotResponse helpResponse=new BotResponse();
//        help(helpResponse);
//        b4.setCallbackData(helpResponse.getSendMessage().getText());
//        buttons2.add(b4);
//
//        List<List<InlineKeyboardButton>> severalButtons = new ArrayList<>();
//        severalButtons.add(buttons1);
//        severalButtons.add(buttons2);
//
//        botResponse.setButtonMarkup(severalButtons);
    }


    private void show(BotResponse botResponse) {
        ParsingBotResponse(botResponse);
        String events = formEventsInfo(0,6, botResponse);
        botResponse.setStringMessage(events);
        botResponse.setSendPhoto(6);
    }

    private String formEventsInfo(int start, int end, BotResponse botResponse){
        String events="";
        for(int i = start; i < end; i++)
        {
            Event event = botResponse.getEvents()[i];
            events+="\n"+(i+1)+". "+"Мероприятие: "+event.getName()+"\nДата: "+event.getDateTime();
            if (i!=end-1)
                events +="\n \r";
        }
        return events;
    }

    private void choose(int numberEvent, BotResponse botResponse) {
        String message = "Такого мероприятия не существует.";
        if (numberEvent == -1)
            message = "Вы ввели некорректный номер мероприятия.";
        ParsingBotResponse(botResponse);
        if (numberEvent>0 && botResponse.getEvents().length >= numberEvent)
            botResponse.setSelectedEvent(botResponse.getEvents()[numberEvent-1]);
        else
            botResponse.setStringMessage(message);
    }

    private void ParsingBotResponse(BotResponse botResponse){
        Parsing parsing = new Parsing();
        parsing.parsing(botResponse);
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
        botResponse.setStringMessage("Введённой команды не существует, вы можете выполнить команду /help, чтобы узнать как пользоваться ботом.");
    }
}
