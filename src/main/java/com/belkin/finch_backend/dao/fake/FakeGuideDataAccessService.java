package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.util.Base62;
import com.belkin.finch_backend.dao.interfaces.GuideDAO;
import com.belkin.finch_backend.model.Guide;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository("guide_fake")
public class FakeGuideDataAccessService implements GuideDAO {

    ArrayList<Guide> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();

    @Override
    public Base62 createGuide(Guide guide) {
        log.info("Creating Guide " + new Gson().toJson(guide) + " in database...");

        identifiers.add(guide.getId());
        database.add(guide);
        return guide.getId();
    }

    @Override
    public List<Guide> readAllGuides() {
        log.info("Reading all guides from database...");

        return database;
    }

    @Override
    public List<Guide> readAllGuidesByAuthorUsername(String authorUsername) {
        log.info("Reading guides of author with username = " + authorUsername + " from database...");

        return database.stream()
                .filter(g -> g.getAuthorUsername().equals(authorUsername))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Guide> readGuideById(Base62 id) {
        log.info("Reading guide by id, where id = " + id.getId());

        return database.stream()
                .filter(g -> g.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean updateGuideById(Base62 id, Guide newGuide) {
        log.info("Updating guide by id, where id = " + id.getId() + " with new data: " + new Gson().toJson(newGuide) + " from database...");

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
        log.info("Deleting guide by id, where id = " + id.getId() + " from database...");

        Guide guide = readGuideById(id).orElse(null);
        return database.remove(guide);
    }

    @Override
    public boolean isPresent(Base62 id) {
        return identifiers.stream().anyMatch(id::equals);
    }
}
