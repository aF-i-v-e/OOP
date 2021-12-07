package ru.praktika95.bot.handle.services.dbService;

import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.hibernate.User;
import ru.praktika95.bot.hibernate.UsersCRUD;

import java.util.*;

import static ru.praktika95.bot.handle.format.FormatDateCalendar.formatDate;

public class DataBaseWorkService {
    private static final UsersCRUD usersCRUD = new UsersCRUD();

    public static boolean setNotificationInDateBase(String period, String chatId, Event selectedEvent) {
        User user = new User(chatId, selectedEvent);
        String[] dateTwo = selectedEvent.getDate().split(" - ");
        String[] date = dateTwo.length == 1 ? dateTwo[0].split("-") : dateTwo[1].split("-");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
        int delta = Objects.equals(period, "день") ? -1 : -7;
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

    public static String deleteByEvent(Event event) {
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
        return DBNoteService.restoreEvents(usersList);
    }

    public static LinkedHashMap<String, LinkedList<Event>> setNotifyDictAndDeleteUsers(String dateNotice) {
        List<User> usersList = usersCRUD.getUsersByDate(dateNotice);
        LinkedHashMap<String, LinkedList<Event>> dict = DBNoteService.getDictionaryChatIdEvent(usersList);
        deleteByUsersList(usersList);
        return dict;
    }

    public static void deleteOldNotes(String dateNotification) {
        List<User> usersList = usersCRUD.getUsersWithLessDateNotice(dateNotification);
        deleteByUsersList(usersList);
    }

    private static void deleteByUsersList(List<User> usersList) {
        for (User user : usersList) {
            usersCRUD.delete(user);
        }
    }
}
