package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Card;
import com.belkin.finch_backend.util.Base62;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("database_card")
public interface CardDAO extends CrudRepository<Card, Base62> {

    List<Card> findCardsByGuide(String guideId);

}