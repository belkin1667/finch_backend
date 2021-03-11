package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.api.dto.AccessType;
import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.exception.UserNotFoundException;
import com.belkin.finch_backend.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;

import static com.belkin.finch_backend.api.dto.AccessType.*;

@Service
public class UserService {

    private final UserDAO userDao;

    public UserService(@Qualifier("user_fake") UserDAO userDao) {
        this.userDao = userDao;
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
                    Set<String> mySubs = meUser.getSubscriptions();
                    Set<String> requestedSubs = requestedUser.getSubscriptions();
                    if (mySubs.contains(requestedUsername) && requestedSubs.contains(myUsername)) {
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
        if (myUsername.equals(requestedUsername)) {
            return new UserResponse(requestedUser, ME_PARTIAL_ACCESS);
        }
        else {
            return new UserResponse(requestedUser, NOT_ME_PARTIAL_ACCESS);
        }
    }

    public UserResponse getUser(String myUsername, String requestedUsername) {
        User requestedUser = userDao.readUserByUsername(requestedUsername)
                .orElseThrow(() -> new UserNotFoundException(requestedUsername));
        AccessType accessType = getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return new UserResponse(requestedUser, accessType);
        else
            return null;
    }
}
