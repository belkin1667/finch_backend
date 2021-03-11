package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardDAO {

    List<Card> readAllCards();

    List<Card> readCardsByGuideId(UUID guideId);

    Optional<Card> readCardById(UUID guideId, UUID id);

    boolean createCard(Card card);

    boolean updateCardById(UUID guideId, UUID id, Card card);

    boolean deleteCardById(UUID guideId, UUID id);
}
