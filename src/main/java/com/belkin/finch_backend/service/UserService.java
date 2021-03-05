package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.UserDAO;
import com.belkin.finch_backend.dto.UserProfileDTO;
import com.belkin.finch_backend.exception.UserNotFoundException;
import com.belkin.finch_backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.belkin.finch_backend.dto.UserProfileDTO.UserType.*;

@Service
public class UserService {

    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public boolean addUser(User user) {
        return userDao.insertUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUserByUsername(String username) {
        return userDao.selectUserByUsername(username).orElse(null);
    }

    public boolean deleteUser(String username) {
        return userDao.deleteUserByUsername(username);
    }

    public boolean updateUser(String username, User user) {
        return userDao.updateUserByUsername(username, user);
    }

    public UserProfileDTO getProfile(String myUsername, String requestedUsername) {
        if (myUsername.equals(requestedUsername)) {
            User user = userDao.selectUserByUsername(myUsername)
                    .orElseThrow(() -> new UserNotFoundException(myUsername));
            return UserProfileDTO.getUserProfileDTO(user, ME);
        } else {
            User requestedUser = userDao.selectUserByUsername(requestedUsername)
                    .orElseThrow(() -> new UserNotFoundException(requestedUsername));
            switch (requestedUser.getProfileAccess()) {
                case ALL:
                    return UserProfileDTO.getUserProfileDTO(requestedUser, FULL_ACCESS);
                case MUTUAL_FOLLOWERS:
                    User meUser = userDao.selectUserByUsername(myUsername)
                            .orElseThrow(() -> new UserNotFoundException(myUsername));
                    Set<String> mySubs = meUser.getSubscriptions();
                    Set<String> requestedSubs = requestedUser.getSubscriptions();
                    if (mySubs.contains(requestedUsername) && requestedSubs.contains(myUsername)) {
                        return UserProfileDTO.getUserProfileDTO(requestedUser, FULL_ACCESS);
                    }
                    return UserProfileDTO.getUserProfileDTO(requestedUser, PARTIAL_ACCESS);
                case NONE:
                    return UserProfileDTO.getUserProfileDTO(requestedUser, PARTIAL_ACCESS);
            }
        }
        return null;
    }
}
