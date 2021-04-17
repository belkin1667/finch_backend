package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.model.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository("fake_user")
public class FakeUserDataAccessService implements UserDAO {

    ArrayList<User> database = new ArrayList<>();

    @Override
    public User save(User user) {
        log.info("Creating user " + new Gson().toJson(user) + " in database...");

        if(findById(user.getUsername()).isPresent()) {
            deleteById(user.getUsername());
        }

        database.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("Reading all users from database...");

        return database;
    }

    @Override
    public Iterable<User> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(String username) {
        log.info("Reading user by username, where username = " + username + " from database...");

        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void deleteById(String username) {
        log.info("Deleting user by username, where username = " + username + " from database...");

        Optional<User> maybeUser = findById(username);
        if (maybeUser.isEmpty())
            return;
        database.remove(maybeUser.get());
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Set<User> findByUsernameContainsIgnoreCaseOrTitleContainsIgnoreCase(String username, String title) {
        return null;
    }

    @Override
    public Set<User> findByUsernameContainsIgnoreCase(String username) {
        return null;
    }

    @Override
    public Set<User> findByTitleContainsIgnoreCase(String title) {
        return null;
    }
}
