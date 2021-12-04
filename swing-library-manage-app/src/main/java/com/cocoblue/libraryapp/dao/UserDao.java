package com.cocoblue.libraryapp.dao;

import com.cocoblue.libraryapp.config.HibernateConfig;
import com.cocoblue.libraryapp.dto.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDao {
    private final SessionFactory factory = HibernateConfig.getSessionFactory();

    // INSERT
    public boolean saveUser(User user) {
        final Session session = factory.getCurrentSession();

        try {
            final Transaction transaction = session.beginTransaction();

            session.save(user);
            transaction.commit();

            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    // SELECT BY Username And PW
    public User getUserByUsernameAndPw(String username, String pw) {
        final Session session = factory.getCurrentSession();
        final Transaction transaction = session.beginTransaction();

        final Query query = session.createQuery("FROM user WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", pw);

        final User user = (User) query.uniqueResult();

        session.close();

        return user;
    }
}
