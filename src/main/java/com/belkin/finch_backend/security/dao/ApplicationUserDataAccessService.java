package com.belkin.finch_backend.security.dao;

import com.belkin.finch_backend.security.model.ApplicationUser;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("fake_appuser")
public class ApplicationUserDataAccessService implements ApplicationUserDAO {

    List<ApplicationUser> database = new ArrayList<>();

    @Override
    public ApplicationUser save(ApplicationUser user) {
        log.info("Creating ApplicationUser " + new Gson().toJson(user) + " in database...");
        database.add(user);
        return user;
    }

    @Override
    public List<ApplicationUser> findAll() {
        log.info("Reading all ApplicationUsers from database...");
        return database;
    }

    @Override
    public Iterable<ApplicationUser> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <S extends ApplicationUser> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ApplicationUser> findById(String username) {
        log.info("Reading ApplicationUser by username = " + username + " from database...");

        return database.stream().filter(u -> u.getUsername().equals(username)).findAny();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void deleteById(String username) {
        log.info("Deleting ApplicationUser by username = " + username + " from database...");

        Optional<ApplicationUser> maybeUser = findById(username);
        if (maybeUser.isEmpty())
            return;
        database.remove(maybeUser.get());
    }

    @Override
    public void delete(ApplicationUser entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends ApplicationUser> entities) {

    }

    @Override
    public void deleteAll() {

    }

}
