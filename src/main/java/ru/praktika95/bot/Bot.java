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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                botRequestHandler.getBotAnswer("start", botRequest, botResponse);
                botResponse.createButtons("main", null, false);
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
            System.out.println(botResponse.getStartEvent());
            System.out.println(botRequest.getTypeButtons());
            System.out.println(botRequest.getBotCommand());
            boolean isNext = Objects.equals(botRequest.getBotCommand(), "next");
            if (Objects.equals(botRequest.getTypeButtons(), "category") || isNext){
                createEvents(botRequest, isNext);
                botResponse.setNull();
                System.out.println(1);
                return;
            }
            System.out.println(2);
            executeBotResponse();
        }
        botResponse.setNull();
    }

    private void createEvents(BotRequest botRequest, boolean isNext) {
        String message;
        int start = botResponse.getStartEvent();
        int end = botResponse.getEndEvent();
        System.out.println(botResponse.getStartEvent());
        System.out.println(end);
        for (int i = start; i < end; i++) {
            Event event = botResponse.getEvents()[i];
            message = "\n" + (i + 1) + ". " + "Мероприятие: " + event.getName() + "\nДата: " + event.getDateTime();
            int status = botResponse.map.get(botRequest.getTypeButtons());
            botResponse.setMessage(message);
            botResponse.setSendPhoto(event.getPhoto());
            boolean isEnd = i == end - 1;
            System.out.println(isEnd);
            if (!isNext)
                ++status;
            botResponse.createButtons(getKey(status, botResponse.map), Integer.toString(i), isEnd);
            executeBotResponse();
        }
        botResponse.setStartEvent(end);
        System.out.println(botResponse.getStartEvent());
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
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
