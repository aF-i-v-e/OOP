package ru.praktika95.bot;

public class BotRequestHandler {
    public BotResponse getBotAnswer(BotRequest botRequest) {
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(botRequest.inputText);
        botResponse.setChatId(botRequest.chatId);
        return botResponse;
    }
}
