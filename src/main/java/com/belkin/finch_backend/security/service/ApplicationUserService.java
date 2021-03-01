package com.belkin.finch_backend.security.service;

import com.belkin.finch_backend.security.dao.ApplicationUserDataAccessService;
import com.belkin.finch_backend.security.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserService implements UserDetailsService {

    private ApplicationUserDataAccessService userDetailsDataAccessService;

    @Autowired
    public ApplicationUserService(ApplicationUserDataAccessService userDetailsDataAccessService) {
        this.userDetailsDataAccessService = userDetailsDataAccessService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsDataAccessService.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public List<ApplicationUser> getAllUsers() {
        return userDetailsDataAccessService.selectAllUsers();
    }

    public boolean isUserPresent(String username) {
        //todo: is catching exception is worse than writing separate query to DAO?
        try {
            loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }

    public void addUser(ApplicationUser user) {
        userDetailsDataAccessService.insertUser(user);
    }

    public void deleteUser(String username) {
        userDetailsDataAccessService.deleteUserByUsername(username);
    }
}
