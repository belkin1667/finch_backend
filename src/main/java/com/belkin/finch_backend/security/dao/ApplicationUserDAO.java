package com.belkin.finch_backend.security.dao;

import com.belkin.finch_backend.security.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("database_appuser")
public interface ApplicationUserDAO extends CrudRepository<ApplicationUser, String> {

}

