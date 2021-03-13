package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.GuideRequest;
import com.belkin.finch_backend.api.dto.GuideResponse;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.GuideService;
import com.belkin.finch_backend.util.Base62;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/guides")
public class GuideController {

    private GuideService guideService;
    private JwtTokenVerifier jwt;

    @Autowired
    public GuideController(GuideService guideService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.guideService = guideService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @ApiOperation(value = "Get guide by its id")
    @GetMapping(path = "/{id}")
    public GuideResponse getGuideById(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getGuideById(myUsername, id);
    }

    @ApiOperation(value = "Return user's guides by username", notes = "If {username} == 'me', current authenticated user's guide is returned")
    @GetMapping(path="/u/{username}")
    public List<GuideResponse> getUserGuides(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;

        return guideService.getGuidesByUsername(myUsername, requestedUsername);
    }


    @ApiOperation(value = "Get guide by its child card id")
    @GetMapping(path = "/c/{card_id}")
    public GuideResponse getGuideByCardId(@PathVariable("card_id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getGuideByCardId(id, myUsername);
    }

    @ApiOperation(value = "Create guide written by authorized user", notes = "Id of the guide returned. Note: if id is provided, it is ignored!")
    @PostMapping
    public Base62 addGuide(@RequestBody GuideRequest guideRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        OffsetDateTime dateNow = OffsetDateTime.now(ZoneOffset.UTC);
        Guide guide = new Guide(myUsername, guideRequest.getId(),guideRequest.getTitle(),
                guideRequest.getDescription(), guideRequest.getLocation(), dateNow, guideRequest.getTravelDate(),
                guideRequest.getThumbnailUrl());
        return guideService.addGuide(myUsername, guide);
    }

    @ApiOperation(value = "Edit guide of authorized user", notes = "200 OK if success, 500 Internal Server Error if fail. \n\r" +
            "There are 2 ways to update entity:\n\r" +
            "(Preferred) 1. Provide only updated fields of the entity, which means you shall omit fields (or provide null value) which aren't supposed to be updated\n\r" +
            "2. Provide all fields of the entity: provide old values for non-updated fields and new values for updated fields")
    @PutMapping
    public String editGuide(@RequestBody GuideRequest guideRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        OffsetDateTime dateNow = OffsetDateTime.now(ZoneOffset.UTC);
        Guide guide = new Guide(myUsername, guideRequest.getId(),guideRequest.getTitle(),
                guideRequest.getDescription(), guideRequest.getLocation(), dateNow, guideRequest.getTravelDate(),
                guideRequest.getThumbnailUrl());
        boolean result = guideService.editGuide(myUsername, guide);

        Optional<String> res = Optional.ofNullable(result ? "Success" : null);
        return res.orElseThrow(() -> new RuntimeException("Guide update failed"));
    }

    @ApiOperation(value = "Edit guide of authorized user", notes = "200 OK if success, 500 Internal Server Error if fail")
    @DeleteMapping(path = "/{id}")
    public String deleteGuide(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername =jwt.getRequesterUsername(authorizationHeader);
        boolean result = guideService.deleteGuide(myUsername, id);

        Optional<String> res = Optional.ofNullable(result ? "Success" : null);
        return res.orElseThrow(() -> new RuntimeException("Guide delete failed"));
    }


}
