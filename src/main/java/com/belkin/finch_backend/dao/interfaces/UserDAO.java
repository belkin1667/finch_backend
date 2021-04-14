package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("database_user")
public interface UserDAO extends CrudRepository<User, String> {

}


