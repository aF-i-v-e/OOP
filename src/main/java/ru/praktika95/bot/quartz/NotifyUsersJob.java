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
import java.util.LinkedList;
import java.util.Map;

public class NotifyUsersJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("QuartzNotifyUsersJob run successfully.");
        String date = TimeService.getCurrentTime();
        LinkedHashMap<String, LinkedList<Event>> dictChatIdEvent = DataBaseWorkService.setNotifyDictAndDeleteUsers(date);
        sendNotify(dictChatIdEvent);
    }

    private void sendNotify(LinkedHashMap<String, LinkedList<Event>> dict) {
        for (Map.Entry<String, LinkedList<Event>> entry: dict.entrySet()) {
            String chatId = entry.getKey();
            LinkedList<Event> events = entry.getValue();
            for(Event event : events) {
                Response response = new Response(chatId,event.getNotifyMessage(), event.getPhoto());
                App.getBot().executeResponse(response);
            }
        }
    }
}
