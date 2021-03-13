package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.SubsDAO;
import com.belkin.finch_backend.model.Subscription;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("subs_fake")
public class FakeSubsDataAccessService implements SubsDAO {

    List<Subscription> database = new ArrayList<>();

    private boolean isUserPresent(String username) {
        return database.stream().anyMatch(s -> s.getUsername().equals(username));
    }

    private boolean isUserPresentAsSubscriber(String username) {
        return database.stream().anyMatch(s -> s.getSubscriber().equals(username));
    }

    @Override
    public Set<String> getUserSubscribers(String username) {
        if (isUserPresent(username))
            return database.stream()
                .filter(subscription -> subscription.getUsername().equals(username))
                .map(Subscription::getSubscriber)
                .collect(Collectors.toSet());
        else
            return new HashSet<>();
    }

    @Override
    public Set<String> getUserSubscriptions(String username) {
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
        return getUserSubscribers(username).size();
    }

    @Override
    public Integer getUserSubscriptionCount(String username) {
        return getUserSubscriptions(username).size();
    }

    @Override
    public boolean addSubscription(Subscription subscription) {
        if (isSubscriptionPresent(subscription))
            return true;
        else
            return database.add(subscription);
    }

    @Override
    public boolean removeSubscription(Subscription subscription) {
        if (isSubscriptionPresent(subscription))
            return database.remove(subscription);
        else
            return true;
    }

    @Override
    public boolean isSubscriptionPresent(Subscription subscription) {
        return database.stream().anyMatch(s -> s.equals(subscription));
    }

}
