package ru.praktika95.bot.responseTests;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.praktika95.bot.bot.BotRequest;

public class SetUpTestData {

    private static BotRequest botRequest;
    public static void setBotRequest(String typeButtons, String botCommand){
        Update update = createTestUpdate(typeButtons, botCommand);
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }

    private static Message createTestMessage(){
        Message message = new Message();
        message.setMessageId(689);
        Chat chat = new Chat(111111111l, "private", null, "UserFirstName", "UserLastName", "userName", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        return message;
    }

    private static CallbackQuery createTestCallBackQuery(String typeButtons, String botCommand){
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("1962727177397599840");
        Message message = createTestMessage();
        callbackQuery.setMessage(message);
        callbackQuery.setData(typeButtons + " " + botCommand);
        return callbackQuery;
    }

    private static Update createTestUpdate(String typeButtons, String botCommand){
        CallbackQuery callbackQuery = createTestCallBackQuery(typeButtons, botCommand);
        Update update = new Update(55632457, null, null, null, callbackQuery, null, null, null, null, null, null, null, null, null);
        return update;
    }

    public static BotRequest getBotRequest() {
        return botRequest;
    }
}
