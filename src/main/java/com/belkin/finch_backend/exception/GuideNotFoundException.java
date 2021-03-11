package com.belkin.finch_backend.exception;

import com.belkin.finch_backend.util.Base62;

public class GuideNotFoundException extends ResourceNotFoundException {

    public GuideNotFoundException() {
        super("Guide");
    }

    public GuideNotFoundException(Base62 id) {
        super("Guide", "Base62 Identifier", id.toString());
    }

    public GuideNotFoundException(String id) {
        super("Guide", "id", id);
    }
}
