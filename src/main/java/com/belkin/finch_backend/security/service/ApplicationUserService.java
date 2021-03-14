package com.belkin.finch_backend.security.service;

import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.dao.ApplicationUserDataAccessService;
import com.belkin.finch_backend.security.dto.RegistrationRequest;
import com.belkin.finch_backend.security.exception.UserAlreadyRegisteredException;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.belkin.finch_backend.security.ApplicationUserRole.USER;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDataAccessService userDetailsDataAccessService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(ApplicationUserDataAccessService userDetailsDataAccessService, UserService userService, PasswordEncoder passwordEncoder) {
        this.userDetailsDataAccessService = userDetailsDataAccessService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
        return userDetailsDataAccessService.selectUserByUsername(username).isPresent();
    }

    public boolean addUser(ApplicationUser user) {
        return userDetailsDataAccessService.insertUser(user);
    }

    public boolean deleteUser(String username) {
        return userDetailsDataAccessService.deleteUserByUsername(username);
    }

    public boolean updateUser(String myUsername, ApplicationUser updatedApplicationUser) {
        return userDetailsDataAccessService.updateUserByUsername(myUsername, updatedApplicationUser);
    }

    public ApplicationUser getUserByUsername(String myUsername) {
        return userDetailsDataAccessService.selectUserByUsername(myUsername).orElse(null);
    }

    public boolean registerUser(RegistrationRequest registrationRequest) {
        if (isUserPresent(registrationRequest.getUsername())) {
            throw new UserAlreadyRegisteredException(registrationRequest.getUsername());
        }

        User user = new User(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        boolean result = userService.addUser(user);

        ApplicationUser applicationUser = new ApplicationUser(registrationRequest.getUsername(), registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                USER.getGrantedAuthorities(),
                true, true, true, true);
        result &= addUser(applicationUser);

        return result;
    }
}
