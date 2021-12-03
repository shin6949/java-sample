package com.cocoblue.libraryapp;

import com.cocoblue.libraryapp.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class LibraryApp {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        HibernateConfig.getSessionFactory().close();
    }
}
