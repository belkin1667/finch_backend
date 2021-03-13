package com.belkin.finch_backend.exception.notfound;

import com.belkin.finch_backend.util.Base62;

public class CardNotFoundException extends ResourceNotFoundException {
    public CardNotFoundException(Base62 id) {
        this(id.getId());
    }

    public CardNotFoundException(String id) {
        super("Card", "Id", id);
    }

}
