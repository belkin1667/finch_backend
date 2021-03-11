package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    List<User> readAllUsers();

    Optional<User> readUserByUsername(String username);

    boolean createUser(User user);

    boolean updateUserByUsername(String username, User user);

    boolean deleteUserByUsername(String username);
}
