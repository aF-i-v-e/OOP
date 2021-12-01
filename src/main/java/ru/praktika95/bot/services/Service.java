package ru.praktika95.bot.services;

import ru.praktika95.bot.Event;
import ru.praktika95.bot.hibernate.User;
import ru.praktika95.bot.hibernate.UsersCRUD;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static ru.praktika95.bot.format.FormatDateCalendar.formatDate;

public class Service {
    private static UsersCRUD usersCRUD = new UsersCRUD();

    public static boolean setNotificationInDateBase(String period, String chatId, Event selectedEvent) {
        User user = new User(chatId, selectedEvent);
        String[] dateTwo = selectedEvent.getDate().split(" - ");
        String[] date = dateTwo.length == 1 ? dateTwo[0].split("-") : dateTwo[1].split("-");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
        int delta = period == "день" ? -1 : -7;
        calendar.add(Calendar.DATE, delta);
        String dateNotice = formatDate(calendar);
        user.setEventDateNotice(dateNotice);
        selectedEvent.setDateNotice(dateNotice);
        if (!usersCRUD.existNote(user)) {
            usersCRUD.save(user);
            return true;
        }
        return false;
    }

    public static String deleteFromDB(Event event) {
        User userToDelete = usersCRUD.getById(event.getIdBD());
        String subscriptionDate = userToDelete.getEventDateNotice();
        usersCRUD.delete(userToDelete);
        return  subscriptionDate;
    }

    public static Integer getLastId() {
        return usersCRUD.getLastId();
    }

    public static List<Event> getEventListByChatId(String userChatId) {
        List<User> usersList = usersCRUD.getByChatId(userChatId);
        List<Event> events = restoreEvent(usersList);
        return events;
    }

    private static LinkedList<Event> restoreEvent(List<User> usersList) {
        LinkedList<Event> events = new LinkedList<>();
        for (User user : usersList){
            Event event = new Event(user.getEventPhoto(),user.getEventName(), user.getEventDate(), user.getEventTime(), user.getEventPlace(), user.getEventPrice(), user.getEventUrl());
            event.setDateNotice(user.getEventDateNotice());
            event.setIdBD(user.getId());
            events.add(event);
        }
        return events;
    }
}
