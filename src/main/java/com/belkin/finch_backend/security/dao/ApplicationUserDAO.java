package com.belkin.finch_backend.security.dao;

import com.belkin.finch_backend.security.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface ApplicationUserDAO {

    boolean insertUser(ApplicationUser user);

    List<ApplicationUser> selectAllUsers();

    Optional<ApplicationUser> selectUserByUsername(String username);

    boolean deleteUserByUsername(String username);

    boolean updateUserByUsername(String username, ApplicationUser user);
}
