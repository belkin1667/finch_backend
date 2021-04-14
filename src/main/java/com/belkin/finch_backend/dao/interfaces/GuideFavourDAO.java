package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Favour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("database_guide_favour")
public interface GuideFavourDAO extends CrudRepository<Favour, Integer> {

    List<Favour> findFavoursByUsername(String liker);

    List<Favour> findFavoursByGuide(String guideId);

    long countByGuide(String guideId);

    boolean existsByUsernameAndGuide(String username, String guide);
}
