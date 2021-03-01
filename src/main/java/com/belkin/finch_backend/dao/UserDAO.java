package com.belkin.finch_backend.dao;

import com.belkin.finch_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    boolean insertUser(User user);

    List<User> selectAllUsers();

    Optional<User> selectUserByUsername(String username);

    boolean deleteUserByUsername(String username);

    boolean updateUserByUsername(String username, User user);

}
