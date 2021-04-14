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
@Repository("fake_card")
public class FakeCardDataAccessService implements CardDAO {

    ArrayList<Card> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();


    @Override
    public Card save(Card card) {
        log.info("Creating Card " + new Gson().toJson(card) + " in database...");

        identifiers.add(card.getId());
        database.add(card);
        return card;
    }

    @Override
    public List<Card> findCardsByGuideId(Base62 guideId) {
        log.info("Reading cards of guide with guide_id = " + guideId.getId() + " from database...");

        return database.stream()
                .filter(c -> c.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Card> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Card> findById(Base62 id) {
        log.info("Reading card by id, where id = " + id.getId() + " from database...");

        return database.stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public void deleteById(Base62 id) {
        log.info("Deleting card by id, where id = " + id.getId() + " from database...");

        Optional<Card> maybeCard = findById(id);
        maybeCard.ifPresent(card -> database.remove(card));
    }

    @Override
    public void delete(Card entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsById(Base62 id) {
        return identifiers.stream().anyMatch(id::equals);
    }

    @Override
    public Iterable<Card> findAll() {
        return null;
    }

    @Override
    public Iterable<Card> findAllById(Iterable<Base62> base62s) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
