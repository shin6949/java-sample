package com.cocoblue.libraryapp.dao;

import com.cocoblue.libraryapp.config.HibernateConfig;
import com.cocoblue.libraryapp.dto.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

        final Book book = session.get(Book.class, isbn);

        session.close();

        // 한 개 불러오기
        return book;
    }

    public void updateBook(Book book) {
        final Session session = factory.getCurrentSession();
        final Transaction transaction = session.beginTransaction();

        session.update(book);

        transaction.commit();
        session.close();
    }

    public void deleteBook(Book book) {
        final Session session = factory.getCurrentSession();
        final Transaction transaction = session.beginTransaction();

        session.delete(book);

        transaction.commit();
        session.close();
    }
}
