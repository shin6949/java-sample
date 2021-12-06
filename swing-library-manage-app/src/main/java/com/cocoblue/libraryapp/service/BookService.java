package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.dao.BookDao;
import com.cocoblue.libraryapp.dao.UserDao;
import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;
import lombok.NoArgsConstructor;

import javax.swing.table.DefaultTableModel;
import java.util.List;

import static com.cocoblue.libraryapp.LibraryApp.CURRENT_USER;


@NoArgsConstructor
public class BookService {
    private final BookDao bookDao = new BookDao();
    private final UserDao userDao = new UserDao();

    public Book getBookByIsbn(String isbn) {
        return bookDao.getBookByIsbn(Long.parseLong(isbn));
    }

    // 책 검색 및 Return
    public List<Book> searchBookByName(String name) {
        // 검색 결과 받아오기
        return bookDao.getBooksByName(name);
    }

    // 모든 책의 정보 Return
    public List<Book> getAllBook() {
        return bookDao.getAllBooks();
    }

    // 대출 처리
    public boolean borrowBook(Book book, User user) {
        // 실시간 정보 체크 후 상태 판단
        if(!book.getStatus()) {
            return false;
        }

        // 대출자 정보 설정
        user.setBorrowedBook(book);
        book.setStatus(false);

        bookDao.updateBook(book);

        return true;
    }

    // 책 반납 (관리자 모드에서 강제로 할 경우)
    public Boolean returnBook(User user) {
        Book book = user.getBorrowedBook();
        user.setBorrowedBook(null);

        // 대출 가능 상태로 변경
        book.setStatus(true);
        bookDao.updateBook(book);

        return true;
    }

    // 로그인 한 계정이 대출한 책 반납
    public Boolean returnBook() {
        Book book = CURRENT_USER.getBorrowedBook();
        book.setStatus(true);
        bookDao.updateBook(book);

        CURRENT_USER.setBorrowedBook(null);
        userDao.updateUser(CURRENT_USER);

        return true;
    }

    public void reflectDataOnModel(DefaultTableModel model, List<Book> books) {
        // Table 초기화
        model.setRowCount(0);

        for(Book book : books) {
            String []list = {String.valueOf(book.getIsbn()), book.getName(), book.getAuthor(), book.getStatusToString(), book.getLocation()};
            model.addRow(list);
        }
    }

    public void deleteBook(Book book) {
        bookDao.deleteBook(book);
    }
}
