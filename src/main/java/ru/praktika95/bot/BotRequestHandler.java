package ru.praktika95.bot;

public class BotRequestHandler {

    public BotResponse getBotAnswer(BotRequest botRequest) {
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(botRequest.getTypeButtons(), botRequest.getBotCommand(), botRequest.getStartEvent());
        botResponse.setChatId(botRequest.getChatId());
        return botResponse;
    }

    public BotResponse getBotAnswer(String command, BotRequest botRequest) {
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(command);
        botResponse.setChatId(botRequest.getChatId());
        return botResponse;
    }
}
