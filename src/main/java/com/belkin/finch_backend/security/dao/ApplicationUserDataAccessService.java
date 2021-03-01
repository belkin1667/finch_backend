package com.belkin.finch_backend.security.dao;

import com.belkin.finch_backend.security.model.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationUserDataAccessService implements ApplicationUserDAO {

    List<ApplicationUser> database = new ArrayList<>();

    @Override
    public boolean insertUser(ApplicationUser user) {
        return database.add(user);
    }

    @Override
    public List<ApplicationUser> selectAllUsers() {
        return database;
    }

    @Override
    public Optional<ApplicationUser> selectUserByUsername(String username) {
        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        Optional<ApplicationUser> maybeUser = selectUserByUsername(username);
        if (maybeUser.isEmpty())
            return false;
        database.remove(maybeUser.get());
        return true;
    }

    @Override
    public boolean updateUserByUsername(String username, ApplicationUser user) {
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
