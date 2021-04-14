package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Like;
import com.belkin.finch_backend.util.Base62;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("database_guide_like")
public interface GuideLikeDAO extends CrudRepository<Like, Integer> {

    List<Like> findLikesByUsername(String liker);

    List<Like> findLikesByGuide(String guideId);

    long countByGuide(String guideId);

    boolean existsByUsernameAndGuide(String username, String guide);
}

