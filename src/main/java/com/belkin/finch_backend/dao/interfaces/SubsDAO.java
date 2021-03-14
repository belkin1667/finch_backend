package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Subscription;

import java.util.Set;

public interface SubsDAO {

    Set<String> getUserSubscribers(String username);

    Set<String> getUserSubscriptions(String username);

    Integer getUserSubscribersCount(String username);

    Integer getUserSubscriptionCount(String username);

    boolean addSubscription(Subscription subscription);

    boolean removeSubscription(Subscription subscription);

    boolean isSubscriptionPresent(Subscription subscription);

    boolean isUserPresentAsSubscription(String username);

    boolean isUserPresentAsSubscriber(String username);
}
