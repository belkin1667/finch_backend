package com.belkin.finch_backend.security.dao;

import com.belkin.finch_backend.security.model.ApplicationUser;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ApplicationUserDataAccessService implements ApplicationUserDAO {

    List<ApplicationUser> database = new ArrayList<>();

    @Override
    public boolean insertUser(ApplicationUser user) {
        log.info("Creating ApplicationUser " + new Gson().toJson(user) + " in database...");
        return database.add(user);
    }

    @Override
    public List<ApplicationUser> selectAllUsers() {
        log.info("Reading all ApplicationUsers from database...");
        return database;
    }

    @Override
    public Optional<ApplicationUser> selectUserByUsername(String username) {
        log.info("Reading ApplicationUser by username = " + username + " from database...");

        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        log.info("Deleting ApplicationUser by username = " + username + " from database...");

        Optional<ApplicationUser> maybeUser = selectUserByUsername(username);
        if (maybeUser.isEmpty())
            return false;
        database.remove(maybeUser.get());
        return true;
    }

    @Override
    public boolean updateUserByUsername(String username, ApplicationUser updatedUser) {
        log.info("Updating ApplicationUser by username, where username = " + username + " with new data: " + new Gson().toJson(updatedUser) + " in database...");

        return selectUserByUsername(username).map( oldUser -> {
            int index = database.indexOf(oldUser);
            if (index >= 0) {
                database.set(index, updatedUser);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
