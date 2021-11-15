package ru.praktika95.bot.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.telegram.telegrambots.meta.api.objects.User;

public class UsersCRUD {

    public void save(Users users) {
        Session session = HibernateUtil.getSessionFactory().openSession(); //открываем сессию
        session.beginTransaction();
        session.save(users);
        session.flush();
        session.close();
    }

    public void delete(Users users) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(users);
        session.flush();
        session.close();
    }

    public List<Users> getAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<Users> criteriaQuery = session.getCriteriaBuilder().createQuery(Users.class);
        criteriaQuery.from(Users.class);
        List<Users> users = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return users;
    }

    public Users getById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Users users = session.get(Users.class, id);
        return users;
    }

    public Users getByChatId(String chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Users users = session.get(Users.class, chatId);
        return users;
    }

    public boolean existNote(Users users) {
        Boolean existUser = false;
        List<Users> usersDB = getAll();
        for(Users userDB : usersDB) {
            if (userDB.equals(users)) {
                existUser = true;
                break;
            }
        }
        return existUser;
    }
}
