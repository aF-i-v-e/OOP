package ru.praktika95.bot.handle.services;

import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.hibernate.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DBNoteService {

    public static LinkedList<Event> restoreEvents(List<User> usersList) {
        LinkedList<Event> events = new LinkedList<>();
        for (User user : usersList){
            Event event = restoreEvent(user);
            events.add(event);
        }
        return events;
    }

    public static Event restoreEvent(User user) {
        Event event = new Event(user.getEventPhoto(),user.getEventName(), user.getEventDate(), user.getEventTime(), user.getEventPlace(), user.getEventPrice(), user.getEventUrl());
        event.setDateNotice(user.getEventDateNotice());
        event.setIdBD(user.getId());
        return event;
    }

    public static LinkedHashMap<String, LinkedList<Event>> getDictionaryChatIdEvent(List<User> users) {
        LinkedHashMap<String, LinkedList<Event>> dictChatIdEvent = new LinkedHashMap<>();
        for (User user : users){
            Event event = restoreEvent(user);
            String chatId = user.getChatId();
            if (!dictChatIdEvent.containsKey(chatId))
                dictChatIdEvent.put(chatId, new LinkedList<>());
            LinkedList<Event> oldEvents = dictChatIdEvent.get(chatId);
            oldEvents.add(event);
        }
        return dictChatIdEvent;
    }
}
