package me.cocoblue.hibertest;

import me.cocoblue.hibertest.dao.BookDao;
import me.cocoblue.hibertest.dto.Book;

public class Main {
    public static void main(String[] args) {
        BookDao bookDao = new BookDao();

        Book book = Book.builder()
                .isbn(1)
                .name("테스트")
                .author("홍길동")
                .status(true)
                .location("1층")
                .borrowedUser(null)
                .build();

        bookDao.saveBook(book);

        System.out.println(bookDao.getBookByIsbn(1));
    }
}
