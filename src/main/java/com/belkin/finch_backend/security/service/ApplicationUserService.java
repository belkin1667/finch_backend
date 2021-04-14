package com.belkin.finch_backend.security.service;

import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.ApplicationUserRole;
import com.belkin.finch_backend.security.dao.ApplicationUserDAO;
import com.belkin.finch_backend.security.dao.ApplicationUserDataAccessService;
import com.belkin.finch_backend.security.dto.RegistrationRequest;
import com.belkin.finch_backend.security.exception.UserAlreadyRegisteredException;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.belkin.finch_backend.security.ApplicationUserRole.USER;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDAO userDetailsDAO;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(@Qualifier("database_appuser") ApplicationUserDAO userDetailsDAO,
                                  UserService userService,
                                  PasswordEncoder passwordEncoder) {
        this.userDetailsDAO = userDetailsDAO;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsDAO.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public List<ApplicationUser> getAllUsers() {
        return Lists.newArrayList(userDetailsDAO.findAll());
    }

    public boolean isUserPresent(String username) {
        return userDetailsDAO.findById(username).isPresent();
    }

    public ApplicationUser addUser(ApplicationUser user) {
        return userDetailsDAO.save(user);
    }

    public void deleteUser(String username) {
        userDetailsDAO.deleteById(username);
    }

    public ApplicationUser updateUser(String myUsername, ApplicationUser updatedApplicationUser) {
        if (!myUsername.equals(updatedApplicationUser.getUsername())) {
            userDetailsDAO.deleteById(myUsername);
        }
        return userDetailsDAO.save(updatedApplicationUser);
    }

    public ApplicationUser getUserByUsername(String myUsername) {
        return userDetailsDAO.findById(myUsername).orElse(null);
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        if (isUserPresent(registrationRequest.getUsername())) {
            throw new UserAlreadyRegisteredException(registrationRequest.getUsername());
        }

        User user = new User(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user = userService.addUser(user);

        Set<ApplicationUserRole> userRoleSet = new HashSet<>();
        userRoleSet.add(USER);

        ApplicationUser applicationUser = new ApplicationUser(registrationRequest.getUsername(), registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                userRoleSet,
                true, true, true, true);
        addUser(applicationUser);
    }
}
