package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.GuideLikeDAO;
import com.belkin.finch_backend.model.Like;
import com.belkin.finch_backend.util.Base62;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("guide_favor_fake")
public class FakeGuideFavorDataAccessService implements GuideLikeDAO {

    List<Like> database = new ArrayList<>();

    @Override
    public boolean addLike(Like like) {
        if (isPresent(like))
            return true;
        else
            return database.add(like);
    }

    @Override
    public boolean removeLike(Like like) {
        if(!isPresent(like))
            return true;
        else
            return database.remove(like);
    }

    @Override
    public List<String> getLikers(Base62 id) {
        return database.stream()
                .filter(l -> l.getGuideId().equals(id))
                .map(Like::getUsername)
                .collect(Collectors.toList());
    }

    @Override
    public List<Base62> getLiked(String liker) {
        return database.stream()
                .filter(l -> l.getUsername().equals(liker))
                .map(Like::getGuideId)
                .collect(Collectors.toList());
    }

    @Override
    public int getLikesNumber(Base62 id) {
        return getLikers(id).size();
    }

    @Override
    public boolean isPresent(Like like) {
        return database.contains(like);
    }
}
