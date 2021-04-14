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
@Repository("fake_guide")
public class FakeGuideDataAccessService implements GuideDAO {

    ArrayList<Guide> database = new ArrayList<>();
    ArrayList<Base62> identifiers = new ArrayList<>();

    @Override
    public Guide save(Guide guide) {
        log.info("Creating Guide " + new Gson().toJson(guide) + " in database...");

        identifiers.add(guide.getId());
        database.add(guide);
        return guide;
    }

    @Override
    public List<Guide> findGuidesByAuthorUsername(String authorUsername) {
        log.info("Reading guides of author with username = " + authorUsername + " from database...");

        return database.stream()
                .filter(g -> g.getAuthorUsername().equals(authorUsername))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Guide> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Guide> findById(Base62 id) {
        log.info("Reading guide by id, where id = " + id.getId());

        return database.stream()
                .filter(g -> g.getId().equals(id))
                .findAny();
    }

    @Override
    public void deleteById(Base62 id) {
        log.info("Deleting guide by id, where id = " + id.getId() + " from database...");

        Guide guide = findById(id).orElse(null);
        if (guide == null)
            return;
        database.remove(guide);
    }

    @Override
    public void delete(Guide entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Guide> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsById(Base62 id) {
        return identifiers.stream().anyMatch(id::equals);
    }

    @Override
    public Iterable<Guide> findAll() {
        return null;
    }

    @Override
    public Iterable<Guide> findAllById(Iterable<Base62> base62s) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
