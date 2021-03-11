package com.belkin.finch_backend.api.devapi;

import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.exception.UserAlreadyRegisteredException;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.security.service.ApplicationUserService;
import com.belkin.finch_backend.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.belkin.finch_backend.security.ApplicationUserRole.USER;

@RequestMapping("dev")
@RestController
public class DevController {

    private final UserService userService;
    private final ApplicationUserService applicationUserService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DevController(UserService userService, ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation(value = "Get all users", notes = "Returns all registered application users. Does not return the passwords")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "Get user by username", notes = "Returns user by its username")
    @GetMapping(path = "/users/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @ApiOperation(value = "Creates new registered user", notes = "Emulates 'POST /register' behavior, but allows to provide full user info in one request")
    @PostMapping("/users")
    @ApiImplicitParam(name = "password", value = "To use default password ('password') the parameter can be omitted")
    public HttpStatus createNewUser(@ApiParam(value = "User's password") @RequestParam Optional<String> password,
                                    @RequestBody User user) {
        String actualPassword = password.orElse("password");

        if (applicationUserService.isUserPresent(user.getUsername())) {
            throw new UserAlreadyRegisteredException(user.getUsername());
        }
        userService.addUser(user);

        ApplicationUser applicationUser = new ApplicationUser(user.getUsername(), user.getEmail(),
                passwordEncoder.encode(actualPassword),
                USER.getGrantedAuthorities(),
                true, true, true, true);
        applicationUserService.addUser(applicationUser);
        return HttpStatus.OK;
    }

    @ApiOperation(value = "Deletes user from users database")
    @DeleteMapping(path = "/users/{username}")
    public void deleteUserByUsername(@ApiParam(value = "Username of the deleted user") @PathVariable("username") String username) {
        userService.deleteUser(username);
    }

    @ApiOperation(value = "Updates user description")
    @PutMapping (path = "/users/{username}")
    public void updateUserByUsername(@ApiParam(value = "Username of the updated user") @PathVariable("username") String username,
                                     @ApiParam(value = "New user description") @RequestBody User user) {
        if (user.getUsername().equals(username) || !applicationUserService.isUserPresent(user.getUsername()))
            userService.updateUser(username, user);
        else
            throw new UserAlreadyRegisteredException(user.getUsername());
    }

    @ApiOperation(value = "Clears the users database")
    @DeleteMapping("/users")
    public void clearUsersDatabase() {
        List<String> usernames =  userService.getAllUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        for (String username : usernames) {
            userService.deleteUser(username);
            applicationUserService.deleteUser(username);
        }
    }
    @ApiOperation(value = "Generates mock users")
    @ApiImplicitParam(name = "number", value = "Number of users to generate. If number is not provided, less than 1 or greater than 20, the default value 10 is used")
    @PostMapping("/users/generate")
    public void generateUsers(@RequestParam(required = false) Integer number) {
        StringGenerator generate = new StringGenerator();
        if (number == null || number <= 0 || number > 20)
            number = 10;

        for (int i = 0; i < number; i++) {
            String username = "username_" + generate.randomLatinString(5);
            User user = new User(username, username + "@finch.com");
            user.setPhone(generate.randomNumbersString(10));
            user.setDescription("description_" + generate.randomLatinString(15));
            user.setTitle("title_" + generate.randomLatinString(5));
            if (i % 3 == 0)
                user.setProfileAccess(User.ProfileAccess.ALL);
            else if (i % 3 == 1)
                user.setProfileAccess(User.ProfileAccess.NONE);
            else if (i % 3 == 2)
                user.setProfileAccess(User.ProfileAccess.MUTUAL_FOLLOWERS);

            try {
                createNewUser(Optional.empty(), user);
            } catch (Exception ignored) { i--; }
        }
    }
}
