package com.cocoblue.libraryapp.dao;

import com.cocoblue.libraryapp.config.HibernateConfig;
import com.cocoblue.libraryapp.dto.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BookDao {
    private final SessionFactory factory = HibernateConfig.getSessionFactory();

    public List<Book> getAllBooks() {
        final Session session = factory.getCurrentSession();
        final Query query = session.createQuery("from book");

        final List<Book> result = (List<Book>) query.list();

        session.close();
        return result;
    }

    public List<Book> getBooksByName(String name) {
        final Session session = factory.getCurrentSession();
        final Query query = session.createQuery("from book WHERE name LIKE :name")
                .setParameter("name", "%" + name + "%");

        final List<Book> result = (List<Book>) query.list();

        session.close();
        return result;
    }

    public Book getBookByIsbn(long isbn) {
        final Session session = factory.getCurrentSession();
        session.beginTransaction();

        final Book book = session.get(Book.class, isbn);

        session.close();

        // 한 개 불러오기
        return book;
    }
}
