package ru.praktika95.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplyMarkupResponseTest {

    private CommandHandler commandHandler;
    private BotRequest botRequest;
    private Response response;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        response = new Response();
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

    private Response createCorrectBotResponse(String typeButtons){
        Response correctBotResponse = new Response();
        int status = correctBotResponse.map.get(typeButtons);
        correctBotResponse.createButtons(getKey(++status, response.map), null, false, null);
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


    private void compareReplyMarkupBotResponse(String typeButtons){
        commandHandler.commandHandler(botRequest, response);
        Response correctBotResponse = createCorrectBotResponse(typeButtons);
        assertEquals(correctBotResponse.getKeyboard(), response.getKeyboard());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        compareReplyMarkupBotResponse("main");
    }

    @Test
    void testCorrectTodayCommand() {
        setBotRequest("date", "today");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectTomorrowCommand() {
        setBotRequest("date", "tomorrow");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectThisWeekCommand() {
        setBotRequest("date", "thisWeek");
        compareReplyMarkupBotResponse("date");
    }
    @Test
    void testCorrectNextWeekCommand() {
        setBotRequest("date", "nextWeek");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectThisMonthCommand() {
        setBotRequest("date", "thisMonth");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectNextMonthCommand() {
        setBotRequest("date", "nextMonth");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectEventCommand() {
        setBotRequest("event", "subscribe");
        compareReplyMarkupBotResponse("event");
    }
}
