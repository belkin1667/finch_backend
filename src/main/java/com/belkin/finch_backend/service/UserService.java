package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.UserDAO;
import com.belkin.finch_backend.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean addUser(User user) {
        return userDao.insertUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUserByUsername(String username) {
        return userDao.selectUserByUsername(username).orElse(null);
    }

    public boolean deleteUser(String username) {
        return userDao.deleteUserByUsername(username);
    }

    public boolean updateUser(String username, User user) {
        return userDao.updateUserByUsername(username, user);
    }
}
