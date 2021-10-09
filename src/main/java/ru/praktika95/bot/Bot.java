package ru.praktika95.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Bot extends TelegramLongPollingBot {

    private String userName;
    private String token;

    public Bot(String botUserName, String token) {
        this.userName = botUserName;
        this.token = token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            BotRequest botRequest = new BotRequest(update);
            BotRequestHandler botRequestHandler = new BotRequestHandler();
            BotResponse botResponse = botRequestHandler.getBotAnswer(botRequest);
            if (botResponse.getSendPhoto().getPhoto() != null)
                executePhoto(botResponse);
            else
                executeMessage(botResponse);
        }
    }

    public void executePhoto(BotResponse botResponse){
        botResponse.getSendPhoto().setCaption(botResponse.getStringMessage());
        try{
            execute(botResponse.getSendPhoto());
        } catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void executeMessage(BotResponse botResponse) {
        botResponse.setSendMessage(botResponse.getStringMessage());
        try {
            execute(botResponse.getSendMessage());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void botConnect() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
