package ru.praktika95.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplyMarkupBotResponseTest {

    private CommandHandler commandHandler;
    private BotResponse botResponse;
    private BotRequest botRequest;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        botResponse = new BotResponse();
    }

    private void setBotRequest(String typeButtons, String botCommand){
        Update update = createTestUpdate(typeButtons, botCommand);
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }

    private Message createTestMessage(){
        Message message = new Message();
        message.setMessageId(689);
        Chat chat = new Chat(454652745l, "private", null, "UserFirstName", "UserLastName", "userName", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        return message;
    }

    private CallbackQuery createTestCallBackQuery(String typeButtons, String botCommand){
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("1962727177397599840");
        Message message = createTestMessage();
        callbackQuery.setMessage(message);
        callbackQuery.setData(typeButtons + " " + botCommand);
        return callbackQuery;
    }

    private Update createTestUpdate(String typeButtons, String botCommand){
        CallbackQuery callbackQuery = createTestCallBackQuery(typeButtons, botCommand);
        Update update = new Update(55632457, null, null, null, callbackQuery, null, null, null, null, null, null, null, null, null);
        return update;
    }

    private BotResponse createCorrectBotResponse(String typeButtons){
        BotResponse correctBotResponse = new BotResponse();
        int status = correctBotResponse.map.get(typeButtons);
        correctBotResponse.createButtons(getKey(++status, botResponse.map), null, false);
        return correctBotResponse;
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void compareStringBotResponse(String correctAnswer){
        commandHandler.commandHandler(botRequest, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    private void compareReplyMarkupBotResponse(String typeButtons){
        commandHandler.commandHandler(botRequest, botResponse);
        BotResponse correctBotResponse = createCorrectBotResponse(typeButtons);
        assertEquals(correctBotResponse.getSendMessage().getReplyMarkup(), botResponse.getSendMessage().getReplyMarkup());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        compareStringBotResponse("Выберите дату");
        compareReplyMarkupBotResponse("main");
    }

    @Test
    void testCorrectTodayCommand() {
        setBotRequest("date", "today");
        compareReplyMarkupBotResponse("date");
    }
}
