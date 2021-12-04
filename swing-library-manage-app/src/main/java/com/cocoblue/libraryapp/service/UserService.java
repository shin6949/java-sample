package com.cocoblue.libraryapp.service;

import com.cocoblue.libraryapp.dao.UserDao;
import com.cocoblue.libraryapp.dto.User;

import java.time.LocalDate;

public class UserService {
    private final UserDao userDao = new UserDao();

    public boolean register(String name, String birth, String username, String pw) {
        final User user = User.builder()
                .name(name)
                .birthday(LocalDate.parse(birth))
                .username(username)
                .password(pw)
                .build();

        return userDao.saveUser(user);
    }
}
