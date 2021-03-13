package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.api.dto.CardRequest;
import com.belkin.finch_backend.api.dto.CardResponse;
import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.GuideService;
import com.belkin.finch_backend.util.Base62;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final GuideService guideService;
    private final JwtTokenVerifier jwt;


    public CardController(GuideService guideService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.guideService = guideService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @GetMapping(path = "/g/{guide_id}")
    public List<CardResponse> getCardsByGuideId(@PathVariable("guide_id") Base62 guideId, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getCardsByGuideId(guideId, myUsername);
    }

    @GetMapping(path = "/{id}")
    public CardResponse getCardById(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.getCardById(myUsername, id);
    }

    @PostMapping
    public Base62 createCard(@RequestBody CardRequest cardRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        return guideService.createCard(cardRequest, myUsername);
    }

    @PutMapping
    public String editCard(@RequestBody CardRequest cardRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        Card card = new Card(cardRequest.getId(), cardRequest.getGuideId(),
                cardRequest.getThumbnailUrl(), cardRequest.getTitle(), cardRequest.getLocation(), cardRequest.getText(), cardRequest.getTags());
        boolean result = guideService.editCard( myUsername, card);

        if (result)
            return "Success";
        else
            throw new RuntimeException();
    }

    @DeleteMapping(path = "/{id}")
    public String deleteCard(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        String myUsername = jwt.getRequesterUsername(authorizationHeader);

        boolean result = guideService.deleteCardById(id, myUsername);
        if (result)
            return "Success";
        else
            throw new RuntimeException();
    }
}