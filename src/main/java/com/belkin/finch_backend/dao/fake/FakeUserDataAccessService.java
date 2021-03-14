package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.model.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("user_fake")
public class FakeUserDataAccessService implements UserDAO {

    ArrayList<User> database = new ArrayList<>();

    @Override
    public boolean createUser(User user) {
        log.info("Creating user " + new Gson().toJson(user) + " in database...");

        if(readUserByUsername(user.getUsername()).isPresent())
            return false;

        return database.add(user);
    }

    @Override
    public List<User> readAllUsers() {
        log.info("Reading all users from database...");

        return database;
    }

    @Override
    public Optional<User> readUserByUsername(String username) {
        log.info("Reading user by username, where username = " + username + " from database...");

        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean updateUserByUsername(String username, User updatedUser) {
        log.info("Updating user by username, where username = " + username + " with new data: " + new Gson().toJson(updatedUser) + " from database...");

        return readUserByUsername(username).map(oldUser -> {
            int index = database.indexOf(oldUser);
            if (index >= 0) {
                database.set(index, updatedUser);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        log.info("Deleting user by username, where username = " + username + " from database...");

        Optional<User> maybeUser = readUserByUsername(username);
        if (maybeUser.isEmpty())
            return false;
        database.remove(maybeUser.get());
        return true;
    }

}
