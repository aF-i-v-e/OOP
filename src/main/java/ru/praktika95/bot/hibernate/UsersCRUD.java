package ru.praktika95.bot.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;

import org.glassfish.jersey.server.model.Suspendable;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.praktika95.bot.Event;

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

    public boolean existNote(User user) {
        Boolean existUser;
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
}
