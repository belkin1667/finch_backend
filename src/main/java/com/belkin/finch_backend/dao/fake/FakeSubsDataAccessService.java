package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.SubsDAO;
import com.belkin.finch_backend.model.Subscription;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository("subs_fake")
public class FakeSubsDataAccessService implements SubsDAO {

    List<Subscription> database = new ArrayList<>();

    @Override
    public boolean isUserPresentAsSubscription(String username) {
        log.info("Checking if user '" + username + "' present as someone's subscription in database...");

        return database.stream().anyMatch(s -> s.getUsername().equals(username));
    }

    @Override
    public boolean isUserPresentAsSubscriber(String username) {
        log.info("Checking if user '" + username + "' present as someone's subscriber in database...");

        return database.stream().anyMatch(s -> s.getSubscriber().equals(username));
    }

    @Override
    public Set<String> getUserSubscribers(String username) {
        log.info("Reading subscribers of user '" + username + "'from database...");

        if (isUserPresentAsSubscription(username))
            return database.stream()
                .filter(subscription -> subscription.getUsername().equals(username))
                .map(Subscription::getSubscriber)
                .collect(Collectors.toSet());
        else
            return new HashSet<>();
    }

    @Override
    public Set<String> getUserSubscriptions(String username) {
        log.info("Reading subscriptions of user '" + username + "'from database...");

        if (isUserPresentAsSubscriber(username)) {
            return database.stream()
                    .filter(subscription -> subscription.getSubscriber().equals(username))
                    .map(Subscription::getUsername)
                    .collect(Collectors.toSet());
        }
        else {
            return new HashSet<>();
        }
    }

    @Override
    public Integer getUserSubscribersCount(String username) {
        log.info("Reading subscribers count of user '" + username + "'from database...");

        return getUserSubscribers(username).size();
    }

    @Override
    public Integer getUserSubscriptionCount(String username) {
        log.info("Reading subscriptions count of user '" + username + "'from database...");

        return getUserSubscriptions(username).size();
    }

    @Override
    public boolean addSubscription(Subscription subscription) {
        log.info("Creating subscription relation " + new Gson().toJson(subscription) + " in database...");

        if (isSubscriptionPresent(subscription))
            return true;
        else
            return database.add(subscription);
    }

    @Override
    public boolean removeSubscription(Subscription subscription) {
        log.info("Deleting subscription relation " + new Gson().toJson(subscription) + " from database...");

        if (isSubscriptionPresent(subscription))
            return database.remove(subscription);
        else
            return true;
    }

    @Override
    public boolean isSubscriptionPresent(Subscription subscription) {
        log.info("Checking if subscription relation " + new Gson().toJson(subscription) + " is present in database...");

        return database.stream().anyMatch(s -> s.equals(subscription));
    }

}
