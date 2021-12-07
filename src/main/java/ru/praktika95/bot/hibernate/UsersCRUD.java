package ru.praktika95.bot.hibernate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.criteria.*;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jsoup.nodes.Element;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

public class UsersCRUD {

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession(); //открываем сессию
        session.beginTransaction();
        session.save(user);
        session.flush();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(user);
        session.flush();
        session.close();
    }

    public List<User> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        criteriaQuery.from(User.class);
        List<User> users = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return users;
    }

    public List<User> getEqualsUsersFromDB(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        Predicate[] predicates = new Predicate[3];
        predicates[0] = cb.like(root.get("chatId"), user.getChatId());
        predicates[1] = cb.like(root.get("eventDateNotice"), user.getEventDateNotice());
        predicates[2] = cb.like(root.get("eventUrl"), user.getEventUrl());
        cr.select(root).where(predicates);
        Query<User> query = session.createQuery(cr);
        List<User> results = query.getResultList();
        session.close();
        return results;
    }

    public User getById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    public Integer getLastId() {
        List<User> users = getAll();
        User lastUser = users.get(users.size()-1);
        return lastUser.getId();
    }

    public List<User> getByChatId(String chatId) { //получаем всех пользователей с данным chatId
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        Predicate predicate = cb.like(root.get("chatId"),chatId);
        cr.select(root).where(predicate);
        Query<User> query = session.createQuery(cr);
        List<User> results = query.getResultList();
        session.close();
        return results;
    }

    public List<User> getUsersWithLessDateNotice(String dateNotice) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        criteriaQuery.from(User.class);
        List<User> users = session.createQuery(criteriaQuery).getResultList();
        session.close();
        users.removeIf(user -> !checkLessDate(user.getEventDateNotice().split("\\."), dateNotice.split("\\.")));
        return users;
    }

    public List<User> getUsersByDate(String dateNotice) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        Predicate predicate = cb.like(root.get("eventDateNotice"), dateNotice);
        cr.select(root).where(predicate);
        Query<User> query = session.createQuery(cr);
        List<User> results = query.getResultList();
        session.close();
        return results;
    }

    public boolean existNote(User user) {
        boolean existUser;
        List<User> usersDB = getEqualsUsersFromDB(user);
        int listSize = usersDB.size();
        if (listSize == 0)
            existUser = false;
        else if (listSize == 1)
            existUser = true;
        else
        {
            System.out.println("У вас несколько одинаковых записей в базе данных!");
            System.out.println("id c одинаковыми записями: ");
            usersDB.forEach(u -> System.out.println("\n" + u.getId().toString()));
            existUser = true;
        }
        return existUser;
    }

    private boolean checkLessDate(String[] dateLess, String[] date) {
        ZoneId z = ZoneId.of( TimeConstants.zoneId );

        ZonedDateTime dateLessZ = ZonedDateTime.of(Integer.parseInt(dateLess[2]), Integer.parseInt(dateLess[1]),
                Integer.parseInt(dateLess[0]), 0, 0, 0, 0, z);
        ZonedDateTime dateZ = ZonedDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]),
                Integer.parseInt(date[0]), 0, 0, 0, 0, z);

        return dateLessZ.isBefore(dateZ);
    }
}
