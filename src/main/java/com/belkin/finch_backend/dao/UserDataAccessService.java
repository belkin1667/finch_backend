package com.belkin.finch_backend.dao;

import com.belkin.finch_backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDataAccessService implements UserDAO {

    ArrayList<User> database = new ArrayList<>();

    @Override
    public boolean insertUser(User user) {
        return database.add(user);
    }

    @Override
    public List<User> selectAllUsers() {
        return database;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        Optional<User> maybeUser = selectUserByUsername(username);
        if (maybeUser.isEmpty())
            return false;
        database.remove(maybeUser.get());
        return true;
    }

    @Override
    public boolean updateUserByUsername(String username, User user) {
        return selectUserByUsername(username).map( u -> {
            int index = database.indexOf(u);
            if (index >= 0) {
                database.set(index, user);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
