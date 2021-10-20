package ru.praktika95.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Bot extends TelegramLongPollingBot {

    private final String userName;
    private final String token;
    private final BotResponse botResponse;

    public Bot(String botUserName, String token)
    {
        this.userName = botUserName;
        this.token = token;
        this.botResponse = new BotResponse();
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
                botRequestHandler.getBotAnswer("start", botRequest, botResponse);
                botResponse.createButtons("main", null, false);
            }
            else
                botRequestHandler.getBotAnswer("otherCommand", botRequest, botResponse);
            executeBotResponse(botResponse);
        } else {
            botRequestHandler.getBotAnswer(botRequest, botResponse);
            LinkedList<BotResponse> list = botRequestHandler.getNextAnswer(botRequest, botResponse);
            if (list.size()!=0) {
                for (int i = 0; i < list.size(); i++) {
                    executeBotResponse(list.get(i));
                    //botResponse.setNull();
                }
            }
            else
                executeBotResponse(botResponse);
        }
        //botResponse.setNull();
    }

    public void executeBotResponse(BotResponse botR){
        if (botR.getSendPhoto().getPhoto() != null)
            executePhoto(botR);
        else
            executeMessage(botR);
    }

    private void executePhoto(BotResponse botR){
        try{
            execute(botR.getSendPhoto());
        } catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void executeMessage(BotResponse botR)
    {
        try {
            execute(botR.getSendMessage());
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
