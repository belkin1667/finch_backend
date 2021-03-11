package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.util.Base62;
import com.belkin.finch_backend.dao.interfaces.GuideDAO;
import com.belkin.finch_backend.model.Guide;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("guide_fake")
public class FakeGuideDataAccessService implements GuideDAO {

    ArrayList<Guide> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();

    @Override
    public Base62 createGuide(Guide guide) {
        Base62 id;
        do {
            id = Base62.randomBase62();
        } while (identifiers.contains(id));
        identifiers.add(id);
        guide.setId(id);
        database.add(guide);
        return id;
    }

    @Override
    public List<Guide> readAllGuides() {
        return database;
    }

    @Override
    public List<Guide> readAllGuidesByAuthorUsername(String authorUsername) {
        return database.stream()
                .filter(g -> g.getAuthorUsername().equals(authorUsername))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Guide> readGuideById(Base62 id) {
        return database.stream()
                .filter(g -> g.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean updateGuideById(Base62 id, Guide newGuide) {
        Guide guide = readGuideById(id).orElse(null);
        int index = database.indexOf(guide);
        if (index >= 0) {
            newGuide.setId(id);
            database.set(index, newGuide);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteGuideById(Base62 id) {
        Guide guide = readGuideById(id).orElse(null);
        return database.remove(guide);
    }
}
