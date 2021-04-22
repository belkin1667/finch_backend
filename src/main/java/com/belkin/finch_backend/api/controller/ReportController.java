package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.ReportReason;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.GuideService;
import com.belkin.finch_backend.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("report")
public class ReportController {

    private final JwtTokenVerifier jwt;
    private final GuideService guideService;

    @Autowired
    public ReportController(GuideService guideService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.guideService = guideService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @PostMapping
    public void reportGuide(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ReportReason reason) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.report(myUsername, reason);
    }
}
