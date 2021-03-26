package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.CardDAO;
import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.util.Base62;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository("card_fake")
public class FakeCardDataAccessService implements CardDAO {

    ArrayList<Card> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();


    @Override
    public Base62 createCard(Card card) {
        log.info("Creating Card " + new Gson().toJson(card) + " in database...");

        identifiers.add(card.getId());
        database.add(card);
        return card.getId();
    }

    @Override
    public List<Card> readAllCards() {
        log.info("Reading all cards from database...");

        return database;
    }

    @Override
    public List<Card> readCardsByGuideId(Base62 guideId) {
        log.info("Reading cards of guide with guide_id = " + guideId.getId() + " from database...");

        return database.stream()
                .filter(c -> c.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Card> readCardById(Base62 id) {
        log.info("Reading card by id, where id = " + id.getId() + " from database...");

        return database.stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean updateCardById(Base62 id, Card newCard) {
        log.info("Updating card by id, where id = " + id.getId() + " with new data: " + new Gson().toJson(newCard) + " from database...");

        return readCardById(id).map(c -> {
            int index = database.indexOf(c);
            if (index >= 0) {
                database.set(index, newCard);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public boolean deleteCardById(Base62 id) {
        log.info("Deleting card by id, where id = " + id.getId() + " from database...");

        Optional<Card> maybeCard = readCardById(id);
        if (maybeCard.isPresent()) {
            database.remove(maybeCard.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Base62> readCardsIdsByGuideId(Base62 guideId) {
        log.info("Reading card ids of the guide by guide_id = " + guideId.getId() + " from database...");

        return database.stream().filter(c -> c.getGuideId().equals(guideId)).map(Card::getId).collect(Collectors.toList());
    }

    @Override
    public boolean isPresent(Base62 id) {
        return identifiers.stream().anyMatch(id::equals);
    }
}
