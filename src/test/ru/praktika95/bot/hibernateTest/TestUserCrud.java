package ru.praktika95.bot.hibernateTest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.praktika95.bot.hibernate.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TestUserCrud {
    public void save(TestUser testUser) {
        Session session = TestHibernateUtil.getSessionFactory().openSession(); //открываем сессию
        session.beginTransaction();
        session.save(testUser);
        session.flush();
        session.close();
    }

    public void delete(TestUser user) {
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(user);
        session.flush();
        session.close();
    }

    public List<TestUser> getAll() {
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<TestUser> criteriaQuery = session.getCriteriaBuilder().createQuery(TestUser.class);
        criteriaQuery.from(TestUser.class);
        List<TestUser> users = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return users;
    }

    public List<TestUser> getEqualsUsersFromDB(TestUser user){
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TestUser> cr = cb.createQuery(TestUser.class);
        Root<TestUser> root = cr.from(TestUser.class);
        Predicate[] predicates = new Predicate[3];
        predicates[0] = cb.like(root.get("chatId"), user.getChatId());
        predicates[1] = cb.like(root.get("eventDateNotice"), user.getEventDateNotice());
        predicates[2] = cb.like(root.get("eventUrl"), user.getEventUrl());
        cr.select(root).where(predicates);
        Query<TestUser> query = session.createQuery(cr);
        List<TestUser> results = query.getResultList();
        session.close();
        return results;
    }

    public TestUser getById(Integer id) {
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        TestUser user = session.get(TestUser.class, id);
        session.close();
        return user;
    }

    public Integer getLastId() {
        List<TestUser> users = getAll();
        TestUser lastUser = users.get(users.size()-1);
        return lastUser.getId();
    }

    public List<TestUser> getByChatId(String chatId) { //получаем всех пользователей с данным chatId
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TestUser> cr = cb.createQuery(TestUser.class);
        Root<TestUser> root = cr.from(TestUser.class);
        Predicate predicate = cb.like(root.get("chatId"),chatId);
        cr.select(root).where(predicate);
        Query<TestUser> query = session.createQuery(cr);
        List<TestUser> results = query.getResultList();
        session.close();
        return results;
    }

    public List<TestUser> getUsersByDate(String dateNotice) {
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TestUser> cr = cb.createQuery(TestUser.class);
        Root<TestUser> root = cr.from(TestUser.class);
        Predicate predicate = cb.like(root.get("eventDateNotice"), dateNotice);
        cr.select(root).where(predicate);
        Query<TestUser> query = session.createQuery(cr);
        List<TestUser> results = query.getResultList();
        session.close();
        return results;
    }

    public boolean existNote(TestUser user) {
        boolean existUser;
        List<TestUser> usersDB = getEqualsUsersFromDB(user);
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

    public void removeAllInstances() {
        Session session = TestHibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("truncate table test").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
