package com.cocoblue.libraryapp;

import com.cocoblue.libraryapp.config.HibernateConfig;
import com.cocoblue.libraryapp.dto.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class LibraryApp {
    public static User CURRENT_USER = null;

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        HibernateConfig.getSessionFactory().close();
    }
}
