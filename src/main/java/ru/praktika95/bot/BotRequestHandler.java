package ru.praktika95.bot;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class BotRequestHandler {

    private CommandHandler comH;

    public BotRequestHandler(){
        comH = new CommandHandler();
    }

    public void getBotAnswer(BotRequest botRequest, BotResponse botResponse) {
        //CommandHandler comH = new CommandHandler();
        comH.commandHandler(botRequest, botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }

    public void getBotAnswer(String command, BotRequest botRequest, BotResponse botResponse) {
        //CommandHandler comH = new CommandHandler();
        comH.commandHandler(command, botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }

    public LinkedList<BotResponse> getNextAnswer(BotRequest botRequest, BotResponse botResponse){
        //CommandHandler comH = new CommandHandler();
        boolean isNext = Objects.equals(botRequest.getBotCommand(), "next");
        if (Objects.equals(botRequest.getTypeButtons(), "category") || isNext){
            return comH.createEvents(botRequest, botResponse, isNext);
            //botResponse.setNull();
           // return;
        }
        return new LinkedList<BotResponse>();
    }
}
