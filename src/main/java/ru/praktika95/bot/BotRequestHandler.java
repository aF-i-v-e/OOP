package ru.praktika95.bot;

public class BotRequestHandler {

    public void getBotAnswer(BotRequest botRequest, BotResponse botResponse) {
        CommandHandler comH = new CommandHandler();
        comH.commandHandler(botRequest.getTypeButtons(), botRequest.getBotCommand(), botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }

    public void getBotAnswer(String command, BotRequest botRequest, BotResponse botResponse) {
        CommandHandler comH = new CommandHandler();
        comH.commandHandler(command, botResponse);
        botResponse.setChatId(botRequest.getChatId());
    }
}
