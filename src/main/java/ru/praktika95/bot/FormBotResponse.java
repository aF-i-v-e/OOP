package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class FormBotResponse {
    public BotResponse getBotAnswer(Update update)
    {
        String chatId = update.getMessage().getChatId().toString();
        String inputText = update.getMessage().getText();
        CommandHandler comH = new CommandHandler();
        BotResponse botResponse = comH.commandHandler(inputText);
        botResponse.setChatId(chatId);
        return botResponse;
    }
}
