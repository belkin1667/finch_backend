package com.belkin.finch_backend.api;

import com.belkin.finch_backend.dto.UserProfileDTO;
import com.belkin.finch_backend.security.jwt.JwsDecoder;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RequestMapping("profile")
@RestController
public class UserController {

    private JwtConfig jwtConfig;
    private UserService userService;
    private SecretKey secretKey;

    @Autowired
    public UserController(UserService userService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
        this.secretKey = secretKey;
    }

    @ApiOperation(value = "Return user data by username", notes = "If username is \"not my\" username user data is returned only if requester has access to it")
    @GetMapping(path = "/{username}")
    public UserProfileDTO getProfile(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        JwsDecoder decoder = new JwsDecoder(token, secretKey);
        String myUsername = decoder.getBody().getSubject();

        return userService.getProfile(myUsername, requestedUsername);
    }
}
