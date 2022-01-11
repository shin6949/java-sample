package me.cocoblue.hibertest.dao;

import me.cocoblue.hibertest.config.HibernateConfig;
import me.cocoblue.hibertest.dto.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BookDao {
    private final SessionFactory factory = HibernateConfig.getSessionFactory();

    public Book getBookByIsbn(long isbn) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        Book book = (Book) session.get(Book.class, isbn);

        session.close();

        // 한 개 불러오기
        return book;
    }

    public void saveBook(Book book) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        session.save(book);

        session.close();
    }
}
