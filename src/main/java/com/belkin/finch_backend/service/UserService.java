package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.interfaces.SubsDAO;
import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.api.dto.AccessType;
import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.exception.alreadyexist.SelfSubscribeException;
import com.belkin.finch_backend.exception.notfound.UserNotFoundException;
import com.belkin.finch_backend.model.Subscription;
import com.belkin.finch_backend.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.belkin.finch_backend.api.dto.AccessType.*;

@Service
public class UserService {

    private final UserDAO userDao;
    private final SubsDAO subsDao;

    public UserService(@Qualifier("user_fake") UserDAO userDao,
                       @Qualifier("subs_fake") SubsDAO subsDao) {
        this.userDao = userDao;
        this.subsDao = subsDao;
    }

    public boolean addUser(User user) {
        return userDao.createUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.readAllUsers();
    }

    public User getUserByUsername(String username) {
        return userDao.readUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public boolean deleteUser(String username) {
        return userDao.deleteUserByUsername(username);
    }

    public boolean updateUser(String username, User user) {
        return userDao.updateUserByUsername(username, user);
    }

    public AccessType getAccessType(String myUsername, String requestedUsername) {
        User meUser = userDao.readUserByUsername(myUsername)
                .orElseThrow(() -> new UserNotFoundException(myUsername));
        if (myUsername.equals(requestedUsername)) {
            return ME_FULL_ACCESS;
        }
        else {
            User requestedUser = userDao.readUserByUsername(requestedUsername)
                    .orElseThrow(() -> new UserNotFoundException(requestedUsername));

            switch (requestedUser.getProfileAccess()) {
                case ALL:
                    return NOT_ME_FULL_ACCESS;
                case MUTUAL_FOLLOWERS:
                    Subscription s1 = new Subscription(myUsername, requestedUsername);
                    Subscription s2 = new Subscription( requestedUsername, myUsername);
                    if (subsDao.isSubscriptionPresent(s1) &&
                            subsDao.isSubscriptionPresent(s2)) {
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
        User requestedUser = userDao.readUserByUsername(requestedUsername)
                .orElseThrow(() -> new UserNotFoundException(requestedUsername));
        AccessType accessType;
        if (myUsername.equals(requestedUsername)) {
            accessType = ME_PARTIAL_ACCESS;
        }
        else {
            accessType = NOT_ME_PARTIAL_ACCESS;
        }

        return new UserResponse(requestedUser,
                subsDao.getUserSubscriptionCount(requestedUsername),
                subsDao.getUserSubscribersCount(requestedUsername),
                accessType);
    }

    public UserResponse getUser(String myUsername, String requestedUsername) {
        User requestedUser = userDao.readUserByUsername(requestedUsername)
                .orElseThrow(() -> new UserNotFoundException(requestedUsername));
        AccessType accessType = getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return new UserResponse(requestedUser,
                    subsDao.getUserSubscriptionCount(requestedUsername),
                    subsDao.getUserSubscribersCount(requestedUsername),
                    accessType);
        else
            return null;
    }

    public Set<String> getSubscribers(String myUsername) {
        return subsDao.getUserSubscribers(myUsername);
    }

    public Set<String> getSubscriptions(String myUsername) {
        return subsDao.getUserSubscriptions(myUsername);
    }

    public boolean subscribe(String myUsername, String subscription) {
        if (myUsername.equals(subscription))
            throw new SelfSubscribeException();
        Subscription s = new Subscription(subscription, myUsername);
        return subsDao.addSubscription(s);
    }

    public boolean unsubscribe(String myUsername, String subscription) {
        if (myUsername.equals(subscription))
            throw new SelfSubscribeException();
        Subscription s = new Subscription(subscription, myUsername);
        return subsDao.removeSubscription(s);
    }
}
