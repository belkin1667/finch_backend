package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Like;
import com.belkin.finch_backend.util.Base62;

import java.util.List;

public interface GuideLikeDAO {

    boolean addLike(Like like);

    boolean removeLike(Like like);

    List<String> getLikers(Base62 id);

    int getLikesNumber(Base62 id);

    boolean isPresent(Like like);
}
