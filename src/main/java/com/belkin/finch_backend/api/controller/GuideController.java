package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.FeedGuideResponse;
import com.belkin.finch_backend.api.dto.GuideRequest;
import com.belkin.finch_backend.api.dto.GuideResponse;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.GuideService;
import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/guides")
public class GuideController {

    private final Gson gson = new Gson();
    private final GuideService guideService;
    private final JwtTokenVerifier jwt;

    @Autowired
    public GuideController(GuideService guideService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.guideService = guideService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @ApiOperation(value = "Get guide by its id")
    @GetMapping(path = "/{id}")
    public GuideResponse getGuideById(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /guides/{id}, where id='" + id.getId() + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        return guideService.getGuideById(myUsername, id);
    }

    @ApiOperation(value = "Return user's guides by username", notes = "If {username} == 'me', current authenticated user's guide is returned")
    @GetMapping(path = "/u/{username}")
    public List<String> getUserGuides(@PathVariable("username") String requestedUsername, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /guides/u/{username}, where username='" + requestedUsername + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        if (requestedUsername.equals("me"))
            requestedUsername = myUsername;

        //return guideService.getGuidesByUsername(myUsername, requestedUsername);
        return guideService.getGuideIdsByUsername(requestedUsername);
    }


    @ApiOperation(value = "Get guide by its child card id")
    @GetMapping(path = "/c/{card_id}")
    public GuideResponse getGuideByCardId(@PathVariable("card_id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /guides/c/{card_id}, where card_id='" + id.getId() + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getGuideByCardId(id, myUsername);
    }

    @ApiOperation(value = "Create guide written by authorized user", notes = "Id of the guide returned. Note: if id is provided, it is ignored!")
    @PostMapping
    public Base62 addGuide(@RequestBody GuideRequest guideRequest, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("POST /guides/ with header Authorization = '" + authorizationHeader + "' and Body: " + gson.toJson(guideRequest));

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        OffsetDateTime dateNow = OffsetDateTime.now(ZoneOffset.UTC);
        Guide guide = new Guide(myUsername, guideRequest.getId(), guideRequest.getTitle(),
                guideRequest.getDescription(), guideRequest.getLocation(), dateNow, guideRequest.getTravelDate(),
                guideRequest.getThumbnailUrl(), guideRequest.getTags());
        return guideService.addGuide(myUsername, guide);
    }

    @ApiOperation(value = "Edit guide of authorized user", notes = "200 OK if success, 500 Internal Server Error if fail. \n\r" +
            "There are 2 ways to update entity:\n\r" +
            "(Preferred) 1. Provide only updated fields of the entity, which means you shall omit fields (or provide null value) which aren't supposed to be updated\n\r" +
            "2. Provide all fields of the entity: provide old values for non-updated fields and new values for updated fields")
    @PutMapping
    public void editGuide(@RequestBody GuideRequest guideRequest, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("PUT /guides/ with header Authorization = '" + authorizationHeader + "' and Body: " + gson.toJson(guideRequest));

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        OffsetDateTime dateNow = OffsetDateTime.now(ZoneOffset.UTC);
        Guide guide = new Guide(myUsername, guideRequest.getId(), guideRequest.getTitle(),
                guideRequest.getDescription(), guideRequest.getLocation(), dateNow, guideRequest.getTravelDate(),
                guideRequest.getThumbnailUrl(), guideRequest.getTags());
        guideService.editGuide(myUsername, guide);
    }

    @ApiOperation(value = "Edit guide of authorized user", notes = "200 OK if success, 500 Internal Server Error if fail")
    @DeleteMapping(path = "/{id}")
    public void deleteGuide(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("DELETE /guides/{id}, where id='" + id.getId() + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.deleteGuide(myUsername, id);
    }





    /* ========================= LIKES ========================= */

    @GetMapping("/likes/{id}")
    public boolean hasLike(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.hasLike(myUsername, id);
    }

    @PostMapping("/likes/{id}")
    public void like(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.likeGuide(myUsername, id);
    }


    @DeleteMapping("/likes/{id}")
    public void unlike(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.unlikeGuide(myUsername, id);
    }



    /* ========================= FAVORITES ========================= */

    @GetMapping("/favourites")
    public List<FeedGuideResponse> getFavourites(@RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getUserFavourites(myUsername);
    }

    @GetMapping("/favourites/{id}")
    public boolean hasFavor(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.hasFavour(myUsername, id);
    }

    @PostMapping("/favourites/{id}")
    public void favour(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.favourGuide(myUsername, id);
    }

    @DeleteMapping("/favourites/{id}")
    public void unfavour(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        guideService.unfavourGuide(myUsername, id);
    }

}
