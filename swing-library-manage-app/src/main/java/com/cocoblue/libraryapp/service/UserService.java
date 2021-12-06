package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.dao.UserDao;
import com.cocoblue.libraryapp.dto.Book;
import com.cocoblue.libraryapp.dto.User;

import java.time.LocalDate;

import static com.cocoblue.libraryapp.LibraryApp.CURRENT_USER;

public class UserService {
    private final UserDao userDao = new UserDao();

    public boolean register(String name, LocalDate birth, String username, char[] pw) {
        final User user = User.builder()
                .name(name)
                .birthday(birth)
                .username(username)
                .password(String.valueOf(pw))
                .build();

        return userDao.saveUser(user);
    }

    public void login(String username, char[] password) {
        final User user = userDao.getUserByUsernameAndPw(username, String.valueOf(password));
        if(user != null) {
            CURRENT_USER = user;
        }
    }

    public void loginOut() {
        CURRENT_USER = null;
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByBorrowedBook(Book book) {
        return userDao.getUserByBorrowedBook(book);
    }

    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
