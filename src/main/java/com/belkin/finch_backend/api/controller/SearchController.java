package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchService searchService;
    private final JwtTokenVerifier jwt;

    @Autowired
    public SearchController(SearchService searchService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.searchService = searchService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @GetMapping("/{query}")
    public List<UserResponse> getUsers(@PathVariable("query") String query, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        return searchService.searchUser(myUsername, query);
    }
}
