package com.belkin.finch_backend.service;

import com.belkin.finch_backend.api.dto.CardRequest;
import com.belkin.finch_backend.api.dto.CardResponse;
import com.belkin.finch_backend.dao.interfaces.CardDAO;
import com.belkin.finch_backend.dao.interfaces.GuideDAO;
import com.belkin.finch_backend.api.dto.AccessType;
import com.belkin.finch_backend.api.dto.GuideResponse;
import com.belkin.finch_backend.exception.AccessDeniedException;
import com.belkin.finch_backend.exception.notfound.CardNotFoundException;
import com.belkin.finch_backend.exception.notfound.GuideNotFoundException;
import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GuideService {

    private final GuideDAO guideDAO;
    private final CardDAO cardDAO;
    private final UserService userService;

    @Autowired
    public GuideService(@Qualifier("guide_fake") GuideDAO guideDAO,
                        CardDAO cardDAO, UserService userService) {
        this.guideDAO = guideDAO;
        this.cardDAO = cardDAO;
        this.userService = userService;
    }

    // ========================= Guide API ========================= //

    public GuideResponse getGuideById(String myUsername, Base62 guideId) {
        log.info("Getting guide by id, where id = " + guideId.getId());

        Guide guide = guideDAO.readGuideById(guideId).orElseThrow(() -> new GuideNotFoundException(guideId));

        log.info("Guide " + new Gson().toJson(guide));

        return new GuideResponse(guide, guide.getAuthorUsername().equals(myUsername) ? AccessType.ME_FULL_ACCESS : AccessType.NOT_ME_FULL_ACCESS);
    }

    public List<GuideResponse> getGuidesPreviewByUsername(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.readAllGuidesByAuthorUsername(requestedUsername);
        AccessType accessType;
        if (myUsername.equals(requestedUsername)) {
            accessType = AccessType.ME_PARTIAL_ACCESS;
        } else {
            accessType = AccessType.NOT_ME_PARTIAL_ACCESS;
        }
        return requestedGuides.stream()
                .map(g -> new GuideResponse(g, accessType))
                .collect(Collectors.toList());
    }

    public List<GuideResponse> getGuidesByUsername(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.readAllGuidesByAuthorUsername(requestedUsername);
        AccessType accessType = userService.getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return requestedGuides.stream()
                    .map(g -> new GuideResponse(g, accessType))
                    .collect(Collectors.toList());
        else
            return null;
    }

    private Base62 getGuideIdByCardId(Base62 cardId) {
        Card card = cardDAO.readCardById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        return card.getGuideId();
    }

    public GuideResponse getGuideByCardId(Base62 cardId, String myUsername) {
        Base62 guideId = getGuideIdByCardId(cardId);
        ;
        return getGuideById(myUsername, guideId);
    }

    public Base62 addGuide(String myUsername, Guide guide) {

        log.info("Generating unique base62 id for card...");
        Base62 id;
        do {
            id = Base62.randomBase62();
            log.info("Trying identifier " + id.getId());
        } while (guideDAO.isPresent(id));
        log.info("Unique guide identifier generated: " + id.getId());

        guide.setId(id);
        guide.setAuthorUsername(myUsername);
        return guideDAO.createGuide(guide);
    }

    public boolean editGuide(String myUsername, Guide newGuide) {
        if (!isUserGuideAuthor(myUsername, newGuide.getId()))
            throw new AccessDeniedException(myUsername);
        Guide oldGuide = guideDAO.readGuideById(newGuide.getId()).orElseThrow(() -> new GuideNotFoundException(newGuide.getId()));
        oldGuide.edit(newGuide);
        return guideDAO.updateGuideById(newGuide.getId(), oldGuide);
    }

    public boolean deleteGuide(String myUsername, Base62 guideId) {
        if (!isUserGuideAuthor(myUsername, guideId))
            throw new AccessDeniedException(myUsername);
        return guideDAO.deleteGuideById(guideId) && deleteCardsByGuideId(guideId);
    }

    public boolean isUserGuideAuthor(String username, Base62 id) {
        Guide guide = guideDAO.readGuideById(id).orElseThrow(() -> new GuideNotFoundException(id));
        boolean res = guide.getAuthorUsername().equals(username);
        return res;
    }


    // ========================= Card API ========================= //

    public CardResponse getCardById(String myUsername, Base62 id) {
        Base62 guideId = getGuideIdByCardId(id);
        AccessType accessType = getGuideById(myUsername, guideId).getType();
        Card card = cardDAO.readCardById(id).orElseThrow(() -> new CardNotFoundException(id));
        return new CardResponse(card, accessType);
    }

    public List<Base62> getCardsIdsByGuideId(Base62 guideId) {
        return cardDAO.readCardsIdsByGuideId(guideId);
    }

    public List<CardResponse> getCardsByGuideId(Base62 guideId, String myUsername) {
        AccessType accessType = getGuideById(myUsername, guideId).getType();
        return cardDAO.readCardsByGuideId(guideId).stream()
                .map(c -> new CardResponse(c, accessType))
                .collect(Collectors.toList());
    }

    public Base62 createCard(CardRequest cardRequest, String myUsername) {
        if (!isUserGuideAuthor(myUsername, cardRequest.getGuideId()))
            throw new AccessDeniedException(myUsername);

        log.info("Generating unique base62 id for card...");
        Base62 id;
        do {
            id = Base62.randomBase62();
            log.info("Trying identifier " + id.getId());
        } while (cardDAO.isPresent(id));
        log.info("Unique card identifier generated: " + id.getId());

        Card card = new Card(id, cardRequest.getGuideId(), cardRequest.getThumbnailUrl(),
                cardRequest.getTitle(), cardRequest.getLocation(), cardRequest.getContent(), cardRequest.getTags());
        return cardDAO.createCard(card);
    }

    public boolean editCard(String myUsername, Card newCard) {
        if (!isUserCardAuthor(myUsername, newCard.getId()))
            throw new AccessDeniedException(myUsername);
        Card oldGuide = cardDAO.readCardById(newCard.getId()).orElseThrow(() -> new CardNotFoundException(newCard.getId()));
        oldGuide.edit(newCard);
        return cardDAO.updateCardById(newCard.getId(), oldGuide);
    }

    public boolean deleteCardById(Base62 id, String myUsername) {
        if (!isUserCardAuthor(myUsername, id))
            throw new AccessDeniedException(myUsername);
        return cardDAO.deleteCardById(id);
    }

    private boolean deleteCardsByGuideId(Base62 guideId) {
        List<Base62> ids = getCardsIdsByGuideId(guideId);
        if (ids.size() == 0)
            return true;

        return ids.stream()
                .map(cardDAO::deleteCardById)
                .reduce(true, Boolean::logicalAnd);

    }

    public boolean isUserCardAuthor(String username, Base62 cardId) {
        Base62 guideId = getGuideIdByCardId(cardId);
        return isUserGuideAuthor(username, guideId);
    }

    public List<GuideResponse> getGuidesOfSubscriptionsOfUser(String myUsername) {
        List<GuideResponse> guides = new ArrayList<>();
        Set<String> subs = userService.getSubscriptions(myUsername);
        for (String sub : subs) {
            guides.addAll(getGuidesByUsername(myUsername, sub));
        }
        guides.sort(Comparator.comparing(GuideResponse::getCreated));
        return guides;
    }

    public List<String> getGuideIdsOfSubscriptionsOfUser(String myUsername) {
        return getGuidesOfSubscriptionsOfUser(myUsername).stream()
                .map(GuideResponse::getId)
                .collect(Collectors.toList());
    }
}
