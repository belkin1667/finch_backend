package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.SubsDAO;
import com.belkin.finch_backend.model.Subscription;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository("fake_subs")
public class FakeSubsDataAccessService implements SubsDAO {

    List<Subscription> database = new ArrayList<>();

    @Override
    public boolean existsByUsername(String username) {
        log.info("Checking if user '" + username + "' present as someone's subscription in database...");

        return database.stream().anyMatch(s -> s.getUsername().equals(username));
    }

    @Override
    public boolean existsBySubscriber(String username) {
        log.info("Checking if user '" + username + "' present as someone's subscriber in database...");

        return database.stream().anyMatch(s -> s.getSubscriber().equals(username));
    }

    @Override
    public boolean existsByUsernameAndAndSubscriber(String username, String subscriber) {
        return false;
    }


    @Override
    public Iterable<Subscription> findSubscriptionsByUsername(String username) {
        log.info("Reading subscribers of user '" + username + "'from database...");

        if (this.existsByUsername(username))
            return database.stream()
                .filter(subscription -> subscription.getUsername().equals(username))
                .collect(Collectors.toSet());
        else
            return new HashSet<>();
    }

    @Override
    public Iterable<Subscription> findSubscriptionsBySubscriber(String username) {
        log.info("Reading subscriptions of user '" + username + "'from database...");

        if (this.existsBySubscriber(username)) {
            return database.stream()
                    .filter(subscription -> subscription.getSubscriber().equals(username))
                    .collect(Collectors.toSet());
        }
        else {
            return new HashSet<>();
        }
    }

    @Override
    public long countBySubscriber(String subscriber) {
        return 0;
    }

    @Override
    public long countByUsername(String username) {
        return 0;
    }

    @Override
    public Subscription save(Subscription subscription) {
        log.info("Creating subscription relation " + new Gson().toJson(subscription) + " in database...");

        if (!existsByUsernameAndAndSubscriber(subscription.getUsername(), subscription.getUsername())) {
            database.add(subscription);
        }
        return subscription;
    }

    @Override
    public <S extends Subscription> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Subscription> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer id) {
        log.info("Checking if subscription relation with id: " + id + " is present in database...");

        return database.stream().anyMatch(s -> s.getId().equals(id));
    }

    @Override
    public Iterable<Subscription> findAll() {
        return null;
    }

    @Override
    public Iterable<Subscription> findAllById(Iterable<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void delete(Subscription entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Subscription> entities) {

    }

    @Override
    public void deleteAll() {

    }

}
