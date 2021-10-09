package ru.praktika95.bot;

public class BotRequestHandler {

    public BotResponse getBotAnswer(BotRequest botRequest) {
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(botRequest.typeButtons, botRequest.botCommand);
        botResponse.setChatId(botRequest.chatId);
        return botResponse;
    }

    public BotResponse getBotAnswer(String command, BotRequest botRequest) {
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(command);
        botResponse.setChatId(botRequest.chatId);
        return botResponse;
    }
}
