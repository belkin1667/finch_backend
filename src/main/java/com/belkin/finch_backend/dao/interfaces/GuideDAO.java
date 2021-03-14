package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.util.Base62;
import com.belkin.finch_backend.model.Guide;

import java.util.List;
import java.util.Optional;

public interface GuideDAO {

    Base62 createGuide(Guide guide);

    List<Guide> readAllGuides();

    List<Guide> readAllGuidesByAuthorUsername(String authorUsername);

    Optional<Guide> readGuideById(Base62 id);

    boolean updateGuideById(Base62 id, Guide newGuide);

    boolean deleteGuideById(Base62 id);

    boolean isPresent(Base62 id);

}
