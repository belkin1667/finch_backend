package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.GuideFavourDAO;
import com.belkin.finch_backend.model.Favour;
import com.belkin.finch_backend.model.Like;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("fake_guide_favour")
public class FakeGuideFavorDataAccessService implements GuideFavourDAO {

    List<Favour> database = new ArrayList<>();

    @Override
    public Favour save(Favour favour) {
        database.add(favour);
        return favour;
    }

    @Override
    public void delete(Favour favour) {
        database.remove(favour);
    }

    @Override
    public void deleteAll(Iterable<? extends Favour> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Favour> findFavoursByGuide(String guideId) {
        return database.stream()
                .filter(l -> l.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Favour> findFavoursByUsername(String liker) {
        return database.stream()
                .filter(l -> l.getUsername().equals(liker))
                .collect(Collectors.toList());
    }

    @Override
    public long countByGuide(String guideId) {
        return findFavoursByGuide(guideId).size();
    }

    @Override
    public boolean existsByUsernameAndGuide(String username, String guide) {
        return false;
    }

    @Override
    public void deleteByUsernameAndGuide(String myUsername, String guide) {

    }

    @Override
    public <S extends Favour> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Favour> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer like) {
        return database.contains(like);
    }

    @Override
    public Iterable<Favour> findAll() {
        return null;
    }

    @Override
    public Iterable<Favour> findAllById(Iterable<Integer> likes) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
