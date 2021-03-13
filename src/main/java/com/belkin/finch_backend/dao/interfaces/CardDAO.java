package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.util.Base62;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardDAO {

    List<Card> readAllCards();

    List<Card> readCardsByGuideId(Base62 guideId);

    Optional<Card> readCardById(Base62 id);

    Base62 createCard(Card card);

    boolean updateCardById(Base62 id, Card card);

    boolean deleteCardById(Base62 id);

    List<Base62> readCardsIdsByGuideId(Base62 guideId);
}
