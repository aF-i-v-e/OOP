package ru.praktika95.bot.notifier;

import ru.praktika95.bot.App;
import ru.praktika95.bot.Event;
import ru.praktika95.bot.Response;
import ru.praktika95.bot.service.DataBaseWorkService;
import java.util.LinkedHashMap;
import java.util.Map;

public class FindNodeServiceImpl implements FindNodeService {
    @Override
    public void findNode(String date) {
        LinkedHashMap<String, Event> dictChatIdEvent = DataBaseWorkService.setNotifyDictAndDeleteUsers(date);
        notify(dictChatIdEvent);
    }

    private void notify(LinkedHashMap<String, Event> dict) {
        for (Map.Entry<String, Event> entry: dict.entrySet()) {
            String chatId = entry.getKey();
            Event event = entry.getValue();
            Response response = new Response(chatId,event.getNotifyMessage(), event.getPhoto());
            App.getBot().executeResponse(response);
        }
    }
}
