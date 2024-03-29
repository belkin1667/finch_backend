package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.interfaces.SubsDAO;
import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.api.dto.AccessType;
import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.exception.alreadyexist.SelfSubscribeException;
import com.belkin.finch_backend.exception.notfound.UserNotFoundException;
import com.belkin.finch_backend.model.Subscription;
import com.belkin.finch_backend.model.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.belkin.finch_backend.api.dto.AccessType.*;

@Service
public class UserService {

    private final UserDAO userDao;
    private final SubsDAO subsDao;

    public UserService(@Qualifier("database_user") UserDAO userDao,
                       @Qualifier("database_subs") SubsDAO subsDao) {
        this.userDao = userDao;
        this.subsDao = subsDao;
    }

    public User addUser(User user) {
        return userDao.save(user);
    }

    public List<User> getAllUsers() {
        return Lists.newArrayList(userDao.findAll());
    }

    public User getUserByUsername(String username) {
        return userDao.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void deleteUser(String username) {
        userDao.deleteById(username);
    }

    public User updateUser(String username, User user) {
        userDao.deleteById(username);
        return userDao.save(user);
    }

    public AccessType getAccessType(String myUsername, String requestedUsername) {
        User meUser = userDao.findById(myUsername)
                .orElseThrow(() -> new UserNotFoundException(myUsername));
        if (myUsername.equals(requestedUsername)) {
            return ME_FULL_ACCESS;
        }
        else {
            User requestedUser = userDao.findById(requestedUsername)
                    .orElseThrow(() -> new UserNotFoundException(requestedUsername));

            switch (requestedUser.getProfileAccess()) {
                case ALL:
                    return NOT_ME_FULL_ACCESS;
                case MUTUAL_FOLLOWERS:
                    if (subsDao.existsByUsernameAndSubscriber(myUsername, requestedUsername) &&
                            subsDao.existsByUsernameAndSubscriber(requestedUsername, myUsername)) {
                        return NOT_ME_FULL_ACCESS;
                    }
                    return NOT_ME_PARTIAL_ACCESS;
                case NONE:
                    return NOT_ME_PARTIAL_ACCESS;
            }
        }
        return null;
    }

    public UserResponse getUserPreview(String myUsername, String requestedUsername) {
        User requestedUser = userDao.findById(requestedUsername)
                .orElseThrow(() -> new UserNotFoundException(requestedUsername));
        AccessType accessType;
        if (myUsername.equals(requestedUsername)) {
            accessType = ME_PARTIAL_ACCESS;
        }
        else {
            accessType = NOT_ME_PARTIAL_ACCESS;
        }

        return new UserResponse(requestedUser,
                subsDao.countByUsername(requestedUsername),
                subsDao.countBySubscriber(requestedUsername),
                accessType,
                subsDao.existsByUsernameAndSubscriber(myUsername, requestedUsername));
    }

    public UserResponse getUser(String myUsername, String requestedUsername) {
        User requestedUser = userDao.findById(requestedUsername)
                .orElseThrow(() -> new UserNotFoundException(requestedUsername));
        AccessType accessType = getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return new UserResponse(requestedUser,
                    subsDao.countByUsername(requestedUsername),
                    subsDao.countBySubscriber(requestedUsername),
                    accessType,
                    subsDao.existsByUsernameAndSubscriber(myUsername, requestedUsername));
        else
            return null;
    }

    public Set<String> getSubscribers(String myUsername) {
        return Lists.newArrayList(subsDao.findSubscriptionsBySubscriber(myUsername)).stream().map(Subscription::getUsername).collect(Collectors.toSet());
    }

    public Set<String> getSubscribers(String myUsername, String requestedUsername) {
        AccessType access = getAccessType(myUsername, requestedUsername);
        System.out.println(access.toString());
        switch (access) {
            case ME_FULL_ACCESS:
            case ME_PARTIAL_ACCESS:
            case NOT_ME_FULL_ACCESS:
                return getSubscribers(requestedUsername);
            case NOT_ME_PARTIAL_ACCESS:
            default:
                return Set.of();
        }
    }

    public Set<String> getSubscriptions(String myUsername) {
        return Lists.newArrayList(subsDao.findSubscriptionsByUsername(myUsername)).stream().map(Subscription::getSubscriber).collect(Collectors.toSet());
    }

    public Set<String> getSubscriptions(String myUsername, String requestedUsername) {
        AccessType access = getAccessType(myUsername, requestedUsername);
        switch (access) {
            case ME_FULL_ACCESS:
            case ME_PARTIAL_ACCESS:
            case NOT_ME_FULL_ACCESS:
                return getSubscriptions(requestedUsername);
            case NOT_ME_PARTIAL_ACCESS:
            default:
                return Set.of();
        }
    }

    public Subscription subscribe(String myUsername, String subscription) {
        if (myUsername.equals(subscription))
            throw new SelfSubscribeException();
        Subscription s = new Subscription(myUsername, subscription);
        return subsDao.save(s);
    }

    public void unsubscribe(String myUsername, String subscription) {
        if (myUsername.equals(subscription))
            throw new SelfSubscribeException();

        subsDao.deleteByUsernameAndSubscriber(myUsername, subscription);
    }

    public String getUserProfilePhotoUrlByUsername(String username) {
        return getUserByUsername(username).getProfilePhotoUrl();
    }
}
