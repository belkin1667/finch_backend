package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.User;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("database_user")
public interface UserDAO extends CrudRepository<User, String> {

    Set<User> findByUsernameContainsIgnoreCaseOrTitleContainsIgnoreCase(String username, String title);

    Set<User> findByUsernameContainsIgnoreCase(String username);

    Set<User> findByTitleContainsIgnoreCase(String title);
}


