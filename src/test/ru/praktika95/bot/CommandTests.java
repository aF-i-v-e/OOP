package ru.praktika95.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTests {

    private CommandHandler commandHandler;
    private BotResponse botResponse;
    private BotRequest botRequest;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        botResponse = new BotResponse();
    }

    private void setBotRequest(String typeButtons, String botCommand){
        Update update = getUpdate(typeButtons, botCommand);
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }
    private Update getUpdate(String typeButtons, String botCommand){
        CallbackQuery callback = new CallbackQuery();
        callback.setId("1952727177396599840");
        Message message = new Message();
        message.setMessageId(649);
        Chat chat = new Chat(454654725l, "private", null, "Алиса", "Рушевская", "aRushevskaya", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        callback.setMessage(message);
        callback.setData(typeButtons + " " + botCommand);
        Update update = new Update(55632457, null, null, null, callback, null, null, null, null, null, null, null, null, null);
        return update;
    }
    private void comparatorExtendedCommand(String correctAnswer){
        commandHandler.commandHandler(botRequest, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        comparatorExtendedCommand("Выберите дату");
        BotResponse correctBotResponse = comparatorExtendedCommand("main", "show");
        botResponseComaparator(correctBotResponse);
    }

    private void botResponseComaparator(BotResponse correctBotResponse){
        assertEquals(correctBotResponse.getSendMessage().getReplyMarkup(), botResponse.getSendMessage().getReplyMarkup());
    }
    private BotResponse comparatorExtendedCommand(String typeButtons, String botCommand){
        BotResponse testBotResponse = new BotResponse();
        int status = testBotResponse.map.get(typeButtons);
        testBotResponse.createButtons(getKey(++status, botResponse.map), null, false);
        //testBotResponse.getSendPhoto().getReplyMarkup();
        return testBotResponse;
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }
}
