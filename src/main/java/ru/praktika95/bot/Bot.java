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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                Buttons buttons = new Buttons();
                InlineKeyboardMarkup inlineButtons = buttons.createButtons("main", null, null);
                botRequestHandler.getBotAnswer("start", botRequest, botResponse);
                botResponse.setMarkUp(inlineButtons);
            }
            else
                botRequestHandler.getBotAnswer("otherCommand", botRequest, botResponse);
            executeBotResponse();
        } else {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String[] callbackData = callbackQuery.getData().split(" ");
            botRequest.setTypeButtons(callbackData[0]);
            botRequest.setBotCommand(callbackData[1]);
            botRequestHandler.getBotAnswer(botRequest, botResponse);
            executeBotResponse();
        }
        botResponse.setNullPhoto();
    }

    public void executeBotResponse(){
        if (botResponse.getSendPhoto().getPhoto() != null)
            executePhoto();
        else
            executeMessage();
    }

    private void executePhoto(){
        try{
            execute(botResponse.getSendPhoto());
        } catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void executeMessage()
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
