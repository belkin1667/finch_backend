package com.belkin.finch_backend.dao.fake;


import com.belkin.finch_backend.dao.interfaces.CardDAO;
import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.util.Base62;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("card_fake")
public class FakeCardDataAccessService implements CardDAO {

    ArrayList<Card> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();

    @Override
    public Base62 createCard(Card card) {
        Base62 id;
        do {
            id = Base62.randomBase62();
        } while (identifiers.contains(id));

        identifiers.add(id);
        card.setId(id);
        database.add(card);
        return id;
    }

    @Override
    public List<Card> readAllCards() {
        return database;
    }

    @Override
    public List<Card> readCardsByGuideId(Base62 guideId) {
        return database.stream()
                .filter(c -> c.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Card> readCardById(Base62 id) {
        return database.stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean updateCardById(Base62 id, Card newCard) {
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
        Optional<Card> maybeCard = readCardById(id);
        if (maybeCard.isPresent()) {
            database.remove(maybeCard.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Base62> readCardsIdsByGuideId(Base62 guideId) {
        return database.stream().filter(c -> c.getGuideId().equals(guideId)).map(Card::getId).collect(Collectors.toList());
    }
}
