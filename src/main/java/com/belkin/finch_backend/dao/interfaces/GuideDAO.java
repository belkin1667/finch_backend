package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.util.Base62;
import com.belkin.finch_backend.model.Guide;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("database_guide")
public interface GuideDAO extends CrudRepository<Guide, Base62> {

    List<Guide> findGuidesByAuthorUsername(String authorUsername);

}