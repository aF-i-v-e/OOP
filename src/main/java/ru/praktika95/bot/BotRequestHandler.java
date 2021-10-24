package ru.praktika95.bot;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class BotRequestHandler {

    private final CommandHandler comH;

    public BotRequestHandler(){
        comH = new CommandHandler();
    }

    public void getBotAnswer(BotRequest botRequest, BotResponse botResponse) {
        comH.commandHandler(botRequest, botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }

    public void getBotAnswer(String command, BotRequest botRequest, BotResponse botResponse) {
        comH.commandHandler(command, botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }

    public LinkedList<BotResponse> getNextAnswer(BotRequest botRequest, BotResponse botResponse) {
        boolean isNext = Objects.equals(botRequest.getBotCommand(), "next");
        LinkedList<BotResponse> listResponses = new LinkedList<>();
        if (Objects.equals(botRequest.getTypeButtons(), "category") || isNext){
            listResponses = comH.createEvents(botRequest, botResponse, isNext);
            if (!isNext)
                listResponses.addFirst(getSeparateMessage(botRequest, botResponse));
        }
        return listResponses;
    }

    public BotResponse getSeparateMessage(BotRequest botRequest, BotResponse botResponse) {
        BotResponse helpBotResponse = new BotResponse();
        helpBotResponse.setChatId(botResponse.getSendMessage().getChatId());
        String eventCategory = switch (botRequest.getBotCommand()) {
            case "theatre" -> "Театр";
            case "museum" -> "Музеи";
            case "concert" -> "Концерт";
            case "allEvents" -> "Все мероприятия";
            default -> "";
        };
        helpBotResponse.setMessage("Вы выбрали категорию: " + eventCategory + " ✓");

        return helpBotResponse;
    }
}
