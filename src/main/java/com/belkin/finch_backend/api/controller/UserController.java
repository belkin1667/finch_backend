package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.UserRequest;
import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.security.service.ApplicationUserService;
import com.belkin.finch_backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Optional;

@RequestMapping("users")
@RestController
public class UserController {

    private JwtTokenVerifier jwt;
    private UserService userService;
    private ApplicationUserService applicationUserService;

    @Autowired
    public UserController(UserService userService, ApplicationUserService applicationUserService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.userService = userService;
        this.applicationUserService = applicationUserService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @ApiOperation(value = "Return user profile by username", notes = "Data of maximum possible access level is returned. If {username} == 'me', current authenticated user's profile is returned")
    @GetMapping(path = "/{username}")
    public UserResponse getUser(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;
        return userService.getUser(myUsername, requestedUsername);
    }

    @ApiOperation(value = "Return user profile preview by username", notes = "If {username} == 'me', current authenticated user's profile is returned")
    @GetMapping(path = "/preview/{username}")
    public UserResponse getUserPreview(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;
        return userService.getUserPreview(myUsername, requestedUsername);
    }

    @ApiOperation(value = "Update user", notes =  "200 OK if success, 500 Internal Server Error if fail.\n\r" +
            "There are 2 ways to update entity:\n\r" +
            "(Preferred) 1. Provide only updated fields of the entity, which means you shall omit fields (or provide null value) which aren't supposed to be updated\n\r" +
            "2. Provide all fields of the entity: provide old values for non-updated fields and new values for updated fields")
    @PutMapping(path = "/me")
    public String updateMe(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        User updatedUser = userRequest.getUser(userService.getUserByUsername(myUsername));
        ApplicationUser updatedApplicationUser = userRequest.getApplicationUser(applicationUserService.getUserByUsername(myUsername));

        boolean result = userService.updateUser(myUsername, updatedUser) &&
                applicationUserService.updateUser(myUsername, updatedApplicationUser);

        Optional<String> res = Optional.ofNullable(result ? "Success" : null);
        return res.orElseThrow(() -> new RuntimeException("User update failed"));
    }

    @ApiOperation(value = "Delete user", notes =  "200 OK if success, 500 Internal Server Error if fail")
    @DeleteMapping(path = "/me")
    public String deleteMe(@RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        boolean result = userService.deleteUser(myUsername) && applicationUserService.deleteUser(myUsername);

        Optional<String> res = Optional.ofNullable(result ? "Success" : null);
        return res.orElseThrow(() -> new RuntimeException("User delete failed"));
    }

}
