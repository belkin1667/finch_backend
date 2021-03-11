package com.belkin.finch_backend.dao.fake;


import com.belkin.finch_backend.dao.interfaces.CardDAO;
import com.belkin.finch_backend.model.Card;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("card_fake")
public class FakeCardDataAccessService implements CardDAO {

    ArrayList<Card> database = new ArrayList<>();

    @Override
    public boolean createCard(Card card) {
        if(readCardById(card.getGuideId(), card.getId()).isPresent())
            return false;

        return database.add(card);
    }

    @Override
    public List<Card> readAllCards() {
        return database;
    }

    @Override
    public List<Card> readCardsByGuideId(UUID guideId) {
        return database.stream()
                .filter(c -> c.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Card> readCardById(UUID guideId, UUID id) {
        return readCardsByGuideId(guideId).stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean updateCardById(UUID guideId, UUID id, Card newCard) {
        return readCardById(guideId, id).map(c -> {
            int index = database.indexOf(c);
            if (index >= 0) {
                database.set(index, newCard);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public boolean deleteCardById(UUID guideId, UUID id) {
        Optional<Card> maybeCard = readCardById(guideId,id);
        if (maybeCard.isPresent()) {
            database.remove(maybeCard.get());
            return true;
        }
        return false;
    }
}
