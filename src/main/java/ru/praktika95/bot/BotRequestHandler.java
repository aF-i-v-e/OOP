package ru.praktika95.bot;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class BotRequestHandler {

    private final CommandHandler comH;

    public BotRequestHandler(){
        comH = new CommandHandler();
    }

    public void getBotAnswer(BotRequest botRequest, Response response) {
        comH.commandHandler(botRequest, response);
        response.setChatId(botRequest.getChatId());
    }

    public void getBotAnswer(String command, BotRequest botRequest, Response response) {
        comH.commandHandler(command, response);
        response.setChatId(botRequest.getChatId());
    }

    public LinkedList<Response> getNextAnswer(BotRequest botRequest, Response response) {
        boolean isNext = Objects.equals(botRequest.getBotCommand(), "next");
        boolean isNextMyEvent = Objects.equals(botRequest.getBotCommand(), "nextMyEvent");
        LinkedList<Response> listResponses = new LinkedList<>();
        if (Objects.equals(botRequest.getTypeButtons(), "category") || isNext){
            listResponses = comH.createEvents(botRequest, response, isNext, response.getEvents(), false);
            if (!isNext)
                listResponses.addFirst(getSeparateMessage(botRequest, response));
        }
        else if (isNextMyEvent  || Objects.equals(botRequest.getTypeButtons(), "main")
                && Objects.equals(botRequest.getBotCommand(), "showMyEvents")) {
            listResponses = comH.createEvents(botRequest, response, isNextMyEvent, response.getMyEventsList(), true);
        }
        return listResponses;
    }


    public Response getSeparateMessage(BotRequest botRequest, Response response) {
        Response helpResponse = new Response();
        helpResponse.setChatId(response.getChatId());
        String eventCategory = switch (botRequest.getBotCommand()) {
            case "theatre" -> "Театр";
            case "museum" -> "Музеи";
            case "concert" -> "Концерт";
            case "allEvents" -> "Все мероприятия";
            default -> "";
        };
        helpResponse.setText("Вы выбрали категорию: " + eventCategory + " ✓");
        return helpResponse;
    }
}
