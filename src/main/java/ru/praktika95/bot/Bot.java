package ru.praktika95.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        BotRequestHandler formBotResponse = new BotRequestHandler();
        BotRequest botRequest = new BotRequest(update);
        BotResponse botResponse = formBotResponse.getBotAnswer(botRequest);
        Message message = update.getMessage();
        if (message.hasText())
        {
            if (Objects.equals(message.getText(), "/start")){
                Buttons buttons = new Buttons();
                InlineKeyboardMarkup inlineButtons = buttons.createButtons("category");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                sendMessage.setText("1");
                sendMessage.setReplyMarkup(inlineButtons);
                execute(sendMessage);
            }
            else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                sendMessage.setText("Вы ввели несуществующую команду");
                execute(sendMessage);
            }
        } else {
            String callbackData = message.getReplyMarkup().getKeyboard().get(0).get(0).getCallbackData();
            String[] data = callbackData.split(" ");
            String typeButtons = data[0];
            String botCommand = data[0];
            CommandHandler commandHandler = new CommandHandler();
            commandHandler.commandHandler(typeButtons, botCommand);
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
