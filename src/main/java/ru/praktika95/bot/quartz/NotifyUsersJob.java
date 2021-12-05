package ru.praktika95.bot.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.praktika95.bot.App;
import ru.praktika95.bot.Event;
import ru.praktika95.bot.Response;
import ru.praktika95.bot.service.DataBaseWorkService;
import ru.praktika95.bot.service.TimeService;

import java.util.LinkedHashMap;
import java.util.Map;

public class NotifyUsersJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("QuartzNotifyUsersJob run successfully.");
        String date = TimeService.getCurrentTime();
        LinkedHashMap<String, Event> dictChatIdEvent = DataBaseWorkService.setNotifyDictAndDeleteUsers(date);
        sendNotify(dictChatIdEvent);
    }

    private void sendNotify(LinkedHashMap<String, Event> dict) {
        for (Map.Entry<String, Event> entry: dict.entrySet()) {
            String chatId = entry.getKey();
            Event event = entry.getValue();
            Response response = new Response(chatId,event.getNotifyMessage(), event.getPhoto());
            App.getBot().executeResponse(response);
        }
    }
}
