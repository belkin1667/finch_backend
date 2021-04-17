package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.UserRequest;
import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.security.service.ApplicationUserService;
import com.belkin.finch_backend.service.UserService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Optional;
import java.util.Set;

@RequestMapping("users")
@RestController
@Slf4j
public class UserController {
    private final Gson gson = new Gson();
    private final JwtTokenVerifier jwt;
    private final UserService userService;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public UserController(UserService userService, ApplicationUserService applicationUserService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.userService = userService;
        this.applicationUserService = applicationUserService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @ApiOperation(value = "Return user profile by username", notes = "Data of maximum possible access level is returned. If {username} == 'me', current authenticated user's profile is returned")
    @GetMapping(path = "/{username}")
    public UserResponse getUser(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/{username}, where username='" + requestedUsername + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;
        return userService.getUser(myUsername, requestedUsername);
    }

    @ApiOperation(value = "Return user profile preview by username", notes = "If {username} == 'me', current authenticated user's profile is returned")
    @GetMapping(path = "/preview/{username}")
    public UserResponse getUserPreview(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/preview/{username}, where username='" + requestedUsername + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;
        return userService.getUserPreview(myUsername, requestedUsername);
    }

    @ApiOperation(value = "Update user", notes = "200 OK if success, 500 Internal Server Error if fail.\n\r" +
            "There are 2 ways to update entity:\n\r" +
            "(Preferred) 1. Provide only updated fields of the entity, which means you shall omit fields (or provide null value) which aren't supposed to be updated\n\r" +
            "2. Provide all fields of the entity: provide old values for non-updated fields and new values for updated fields")
    @PutMapping(path = "/me")
    public void updateMe(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("PUT /users/me with header Authorization = '" + authorizationHeader + "' and Body: " + gson.toJson(userRequest));

        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        User updatedUser = userRequest.getUser(userService.getUserByUsername(myUsername));
        ApplicationUser updatedApplicationUser = userRequest.getApplicationUser(applicationUserService.getUserByUsername(myUsername));

        updatedUser = userService.updateUser(myUsername, updatedUser);
        updatedApplicationUser = applicationUserService.updateUser(myUsername, updatedApplicationUser);
    }

    @ApiOperation(value = "Delete user", notes = "200 OK if success, 500 Internal Server Error if fail")
    @DeleteMapping(path = "/me")
    public void deleteMe(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("DELETE /users/me  with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        userService.deleteUser(myUsername);
        applicationUserService.deleteUser(myUsername);
    }




    @PostMapping(path = "/subs/{username}")
    public void subscribe(@PathVariable("username") String subscription, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("POST /users/subs/{username}, where username='" + subscription + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        userService.subscribe(myUsername, subscription);
    }

    @DeleteMapping(path = "/subs/{username}")
    public void unsubscribe(@PathVariable("username") String subscription, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("DELETE /users/subs/{username}, where username='" + subscription + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        userService.unsubscribe(myUsername, subscription);
    }

    @GetMapping(path = "/subs/subscribers")
    public Set<String> getSubscribers(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/subs/subscribers with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return userService.getSubscribers(myUsername);
    }

    @GetMapping(path = "/subs/subscribers/{username}")
    public Set<String> getSubscribers(@PathVariable("username") String username, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/subs/subscribers/" + username + " with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return userService.getSubscribers(myUsername, username);
    }

    @GetMapping(path = "/subs/subscriptions")
    public Set<String> getSubscriptions(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/subs/subscriptions with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return userService.getSubscriptions(myUsername);
    }

    @GetMapping(path =  "/subs/subscriptions/{username}")
    public Set<String> getSubscriptions(@PathVariable("username") String username, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /users/subs/subscriptions/" + username + " with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return userService.getSubscriptions(myUsername, username);
    }

    /*
        others Subscribers
        others Subscriptions
        is subscribed
        is subscribed in user entity
     */
}
