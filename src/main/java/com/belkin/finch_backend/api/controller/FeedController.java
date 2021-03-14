package com.belkin.finch_backend.api.controller;


import com.belkin.finch_backend.api.dto.GuideResponse;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.GuideService;
import com.belkin.finch_backend.service.UserService;
import com.belkin.finch_backend.util.Base62;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("feed")
public class FeedController {

    private final UserService userService;
    private final GuideService guideService;
    private final JwtTokenVerifier jwt;

    @Autowired
    public FeedController(UserService userService, GuideService guideService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.userService = userService;
        this.guideService = guideService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @GetMapping
    public List<String> getFeed(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /feed with header Authorization = " + authorizationHeader);

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getGuideIdsOfSubscriptionsOfUser(myUsername);
    }

    @GetMapping("/full")
    public List<GuideResponse> getFeedFull(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /feed/full with header Authorization = " + authorizationHeader);

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getGuidesOfSubscriptionsOfUser(myUsername);
    }
}
