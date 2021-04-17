package com.belkin.finch_backend.service;

import com.belkin.finch_backend.api.dto.*;
import com.belkin.finch_backend.dao.interfaces.CardDAO;
import com.belkin.finch_backend.dao.interfaces.GuideDAO;
import com.belkin.finch_backend.dao.interfaces.GuideFavourDAO;
import com.belkin.finch_backend.dao.interfaces.GuideLikeDAO;
import com.belkin.finch_backend.exception.AccessDeniedException;
import com.belkin.finch_backend.exception.notfound.CardNotFoundException;
import com.belkin.finch_backend.exception.notfound.GuideNotFoundException;
import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.model.Favour;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.model.Like;
import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GuideService {

    private final GuideDAO guideDAO;
    private final CardDAO cardDAO;
    private final GuideLikeDAO guideLikesDAO;
    private final GuideFavourDAO guideFavorDAO;
    private final UserService userService;

    @Autowired
    public GuideService(@Qualifier("database_guide") GuideDAO guideDAO,
                        @Qualifier("database_card") CardDAO cardDAO,
                        @Qualifier("database_guide_like") GuideLikeDAO guideLikesDAO,
                        @Qualifier("database_guide_favour") GuideFavourDAO guideFavorDAO,
                        UserService userService) {
        this.guideDAO = guideDAO;
        this.cardDAO = cardDAO;
        this.guideLikesDAO = guideLikesDAO;
        this.guideFavorDAO = guideFavorDAO;
        this.userService = userService;
    }

    // ========================= Guide API ========================= //

    public GuideResponse getGuideById(String myUsername, Base62 guideId) {
        log.info("Getting guide by id, where id = " + guideId.getId());

        Guide guide = guideDAO.findById(guideId).orElseThrow(() -> new GuideNotFoundException(guideId));

        log.info("Guide " + new Gson().toJson(guide));

        return new GuideResponse(guide,
                guide.getAuthorUsername().equals(myUsername) ? AccessType.ME_FULL_ACCESS : AccessType.NOT_ME_FULL_ACCESS,
                guideLikesDAO.existsByUsernameAndGuide(myUsername, guideId.getId()),
                guideLikesDAO.countByGuide(guideId.getId()),
                guideFavorDAO.existsByUsernameAndGuide(myUsername, guideId.getId()));
    }

    public List<GuideResponse> getGuidesPreviewByUsername(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.findGuidesByAuthorUsername(requestedUsername);
        AccessType accessType;
        if (myUsername.equals(requestedUsername)) {
            accessType = AccessType.ME_PARTIAL_ACCESS;
        } else {
            accessType = AccessType.NOT_ME_PARTIAL_ACCESS;
        }
        return requestedGuides.stream()
                .map(g ->
                        new GuideResponse(g,
                                accessType,
                                guideLikesDAO.existsByUsernameAndGuide(myUsername, g.getId().getId()),
                                guideLikesDAO.countByGuide(g.getId().getId()),
                                guideFavorDAO.existsByUsernameAndGuide(myUsername, g.getId().getId())))
                .collect(Collectors.toList());
    }

    public List<String> getGuideIdsByUsername(String requestedUsername) {
        List<String> guides = guideDAO.findGuidesByAuthorUsername(requestedUsername).stream()
                .map(Guide::getId)
                .map(Base62::getId)
                .collect(Collectors.toList());
        return guides;
    }

    public List<GuideResponse> getGuidesByUsername(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.findGuidesByAuthorUsername(requestedUsername);
        AccessType accessType = userService.getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return requestedGuides.stream()
                    .map(g ->
                            new GuideResponse(g,
                                    accessType,
                                    guideLikesDAO.existsByUsernameAndGuide(myUsername, g.getId().getId()),
                                    guideLikesDAO.countByGuide(g.getId().getId()),
                                    guideFavorDAO.existsByUsernameAndGuide(myUsername, g.getId().getId())))
                    .collect(Collectors.toList());
        else
            return null;
    }

    private Base62 getGuideIdByCardId(Base62 cardId) {
        Card card = cardDAO.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        return card.getGuideId();
    }

    public GuideResponse getGuideByCardId(Base62 cardId, String myUsername) {
        Base62 guideId = getGuideIdByCardId(cardId);
        return getGuideById(myUsername, guideId);
    }

    public Base62 addGuide(String myUsername, Guide guide) {

        log.info("Generating unique base62 id for card...");
        Base62 id;
        do {
            id = Base62.randomBase62();
            log.info("Trying identifier " + id.getId());
        } while (guideDAO.existsById(id));
        log.info("Unique guide identifier generated: " + id.getId());

        guide.setId(id);
        guide.setAuthorUsername(myUsername);
        return guideDAO.save(guide).getId();
    }

    public void editGuide(String myUsername, Guide newGuide) {
        if (!isUserGuideAuthor(myUsername, newGuide.getId()))
            throw new AccessDeniedException(myUsername);
        Guide oldGuide = guideDAO.findById(newGuide.getId()).orElseThrow(() -> new GuideNotFoundException(newGuide.getId()));
        oldGuide.edit(newGuide);
        guideDAO.save(oldGuide);
    }

    public void deleteGuide(String myUsername, Base62 guideId) {
        if (!isUserGuideAuthor(myUsername, guideId))
            throw new AccessDeniedException(myUsername);
        guideDAO.deleteById(guideId);
        deleteCardsByGuideId(guideId);
    }

    public boolean isUserGuideAuthor(String username, Base62 id) {
        Guide guide = guideDAO.findById(id).orElseThrow(() -> new GuideNotFoundException(id));
        boolean res = guide.getAuthorUsername().equals(username);
        return res;
    }


    /* ========================= Card API ========================= */

    public CardResponse getCardById(String myUsername, Base62 id) {
        Base62 guideId = getGuideIdByCardId(id);
        AccessType accessType = getGuideById(myUsername, guideId).getType();
        Card card = cardDAO.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return new CardResponse(card, accessType);
    }

    public List<Base62> getCardsIdsByGuideId(Base62 guideId) {
        return cardDAO.findCardsByGuide(guideId.getId()).stream().map(Card::getId).collect(Collectors.toList());
    }

    public List<CardResponse> getCardsByGuideId(Base62 guideId, String myUsername) {
        AccessType accessType = getGuideById(myUsername, guideId).getType();
        return cardDAO.findCardsByGuide(guideId.getId()).stream()
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
        } while (cardDAO.existsById(id));
        log.info("Unique card identifier generated: " + id.getId());

        Card card = new Card(id, cardRequest.getGuideId(), cardRequest.getThumbnailUrl(),
                cardRequest.getTitle(), cardRequest.getLocation(), cardRequest.getContent());
        return cardDAO.save(card).getId();
    }

    public void editCard(String myUsername, Card newCard) {
        if (!isUserCardAuthor(myUsername, newCard.getId()))
            throw new AccessDeniedException(myUsername);
        Card oldGuide = cardDAO.findById(newCard.getId()).orElseThrow(() -> new CardNotFoundException(newCard.getId()));
        oldGuide.edit(newCard);
        cardDAO.save(oldGuide);
    }

    public void deleteCardById(Base62 id, String myUsername) {
        if (!isUserCardAuthor(myUsername, id))
            throw new AccessDeniedException(myUsername);
        cardDAO.deleteById(id);
    }

    private void deleteCardsByGuideId(Base62 guideId) {
        List<Base62> ids = getCardsIdsByGuideId(guideId);
        if (ids.size() != 0)
            ids.stream().map(id -> { cardDAO.deleteById(id); return true; });
    }

    public boolean isUserCardAuthor(String username, Base62 cardId) {
        Base62 guideId = getGuideIdByCardId(cardId);
        return isUserGuideAuthor(username, guideId);
    }

    public List<GuideResponse> getGuidesOfSubscriptionsOfUser(String myUsername) {
        List<GuideResponse> guides = new ArrayList<>();
        Set<String> subs = userService.getSubscriptions(myUsername);
        subs.add(myUsername);
        for (String sub : subs) {
            guides.addAll(getGuidesByUsername(myUsername, sub));
        }
        guides.sort(Comparator.comparing(GuideResponse::getCreated));
        return guides;
    }

    public List<GuideResponse> getGuidesOfSubscriptionsOfUser(String myUsername, int page) {
        List<GuideResponse> guides =  getGuidesOfSubscriptionsOfUser(myUsername);
        int offset = page * 10;
        int limit = 10;
        return guides.subList(Math.min(guides.size(), offset), Math.min(guides.size(), offset + limit));
    }

    public List<String> getGuideIdsOfSubscriptionsOfUser(String myUsername) {
        return getGuidesOfSubscriptionsOfUser(myUsername).stream()
                .map(GuideResponse::getId)
                .collect(Collectors.toList());
    }

    public List<FeedGuideResponse> getFeedGuideOfSubscriptionsOfUser(String myUsername) {
        return getGuidesOfSubscriptionsOfUser(myUsername).stream()
            .map(g -> new FeedGuideResponse(g.getId(), g.getAuthorUsername(), userService.getUserProfilePhotoUrlByUsername(g.getAuthorUsername())))
            .collect(Collectors.toList());
    }

    public List<FeedGuideResponse> getFeedGuideOfSubscriptionsOfUser(String myUsername, int page) {
        return getGuidesOfSubscriptionsOfUser(myUsername, page).stream()
                .map(g -> new FeedGuideResponse(g.getId(), g.getAuthorUsername(), userService.getUserProfilePhotoUrlByUsername(g.getAuthorUsername())))
                .collect(Collectors.toList());
    }

    /* ========================= Likes API ========================= */

    public Like likeGuide(String myUsername, Base62 id) {
        return guideLikesDAO.save(new Like(myUsername, id));
    }

    public void unlikeGuide(String myUsername, Base62 id) {
        guideLikesDAO.deleteByUsernameAndGuide(myUsername, id.getId());
    }

    public boolean hasLike(String myUsername, Base62 id) {
        return guideLikesDAO.existsByUsernameAndGuide(myUsername, id.getId());

    }

    /* ========================= Favourites API ========================= */

    public Favour favourGuide(String myUsername, Base62 id) {
        return guideFavorDAO.save(new Favour(myUsername, id));
    }

    public void unfavourGuide(String myUsername, Base62 id) {
        guideFavorDAO.deleteByUsernameAndGuide(myUsername, id.getId());
    }

    public boolean hasFavour(String myUsername, Base62 id) {
        return guideFavorDAO.existsByUsernameAndGuide(myUsername, id.getId());
    }

    public List<FeedGuideResponse> getUserFavourites(String myUsername) {
        return guideFavorDAO.findFavoursByUsername(myUsername).stream()
                .map(Favour::getGuideId)
                .map(this::idToFeedGuideResponseMapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private FeedGuideResponse idToFeedGuideResponseMapper(Base62 id) {
        Optional<Guide> maybeGuide = guideDAO.findById(id);
        if(maybeGuide.isPresent()) {
            String username = maybeGuide.get().getAuthorUsername();
            String profilePhoto = userService.getUserProfilePhotoUrlByUsername(username);
            return new FeedGuideResponse(id.getId(), username, profilePhoto);
        }
        else {
            return null;
        }
    }

}
