package ru.praktika95.bot.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.praktika95.bot.handle.response.Response;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    private final String userName;
    private final String token;
    private final Response response;

    public Bot(String botUserName, String token) {
        this.userName = botUserName;
        this.token = token;
        this.response = new Response();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        BotRequestHandler botRequestHandler = new BotRequestHandler();
        BotRequest botRequest = new BotRequest(update);
        if (update.hasMessage() && update.getMessage().hasText())
        {
            Message message = update.getMessage();
            if (Objects.equals(message.getText(), "/start")){
                botRequestHandler.getBotAnswer("start", botRequest, response);
                response.createButtons("main", null, false, null);
            }
            else
                botRequestHandler.getBotAnswer("otherCommand", botRequest, response);
            executeResponse(response);
        } else {
            botRequestHandler.getBotAnswer(botRequest, response);
            LinkedList<Response> list = botRequestHandler.getNextAnswer(botRequest, response);

            if (list.size() != 0)
                executeResponseList(list);
            else
                executeResponse(response);
        }
        response.setNull();
    }

    private void executeResponseList(LinkedList<Response> list) {
        for (Response value : list) executeResponse(value);
        response.setNull();
    }

    public void executeResponse(Response response) {
        BotResponse botResponse = new BotResponse(response);
        if (response.getPhotoFile() != null)
            executePhotoBotResponse(botResponse);
        else
            executeMessageBotResponse(botResponse);
    }

    private void executePhotoBotResponse(BotResponse botResponse) {
        try{
            execute(botResponse.getSendPhoto());
        } catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void executeMessageBotResponse(BotResponse botResponse)
    {
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
