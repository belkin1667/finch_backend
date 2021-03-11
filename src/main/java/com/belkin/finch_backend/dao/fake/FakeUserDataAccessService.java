package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("user_fake")
public class FakeUserDataAccessService implements UserDAO {

    ArrayList<User> database = new ArrayList<>();

    @Override
    public boolean createUser(User user) {
        if(readUserByUsername(user.getUsername()).isPresent())
            return false;

        return database.add(user);
    }

    @Override
    public List<User> readAllUsers() {
        return database;
    }

    @Override
    public Optional<User> readUserByUsername(String username) {
        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean updateUserByUsername(String username, User updatedUser) {
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
        Optional<User> maybeUser = readUserByUsername(username);
        if (maybeUser.isEmpty())
            return false;
        database.remove(maybeUser.get());
        return true;
    }

}
