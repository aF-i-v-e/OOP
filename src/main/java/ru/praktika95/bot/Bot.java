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

    public Bot(String botUserName, String token)
    {
        this.userName = botUserName;
        this.token = token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        BotRequest botRequest = new BotRequest(update);
        if (update.hasMessage() && update.getMessage().hasText())
        {
            Message message = update.getMessage();
            if (Objects.equals(message.getText(), "/start")){
                System.out.println("1");
                Buttons buttons = new Buttons();
                InlineKeyboardMarkup inlineButtons = buttons.createButtons("category");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(botRequest.chatId);
                sendMessage.setText("1");
                sendMessage.setReplyMarkup(inlineButtons);
                execute(sendMessage);
            }
            else {
                System.out.println("2");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(botRequest.chatId);
                sendMessage.setText("Вы ввели несуществующую команду");
                execute(sendMessage);
            }
        } else {
            System.out.println("3");
            CallbackQuery callbackQuery = update.getCallbackQuery();
            System.out.println(callbackQuery.getMessage());
            String[] callbackData = callbackQuery.getMessage().getReplyMarkup().getKeyboard().get(0).get(0).getCallbackData().split(" ");
            botRequest.setTypeButtons(callbackData[0]);
            botRequest.setBotCommand(callbackData[1]);
            System.out.println("3");
            BotRequestHandler botRequestHandler = new BotRequestHandler();
            BotResponse botResponse = botRequestHandler.getBotAnswer(botRequest);
            System.out.println("3");
            System.out.println(botResponse.getSendMessage());
            execute(botResponse.getSendMessage());
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

    public void executeMessage(BotResponse botResponse)
    {
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
