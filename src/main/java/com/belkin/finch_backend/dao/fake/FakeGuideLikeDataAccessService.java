package com.belkin.finch_backend.dao.fake;

import com.belkin.finch_backend.dao.interfaces.GuideLikeDAO;
import com.belkin.finch_backend.model.Like;
import com.belkin.finch_backend.util.Base62;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository("fake_guide_like")
public class FakeGuideLikeDataAccessService implements GuideLikeDAO {

    List<Like> database = new ArrayList<>();

    @Override
    public Like save(Like like) {
        database.add(like);
        return like;
    }

    @Override
    public void delete(Like like) {
        database.remove(like);
    }

    @Override
    public void deleteAll(Iterable<? extends Like> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Like> findLikesByGuide(String guideId) {
        return database.stream()
                .filter(l -> l.getGuideId().equals(guideId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Like> findLikesByUsername(String liker) {
        return database.stream()
                .filter(l -> l.getUsername().equals(liker))
                .collect(Collectors.toList());
    }

    @Override
    public long countByGuide(String guideId) {
        return findLikesByGuide(guideId).size();
    }

    @Override
    public boolean existsByUsernameAndGuide(String username, String guide) {
        return false;
    }

    @Override
    public void deleteByUsernameAndGuide(String myUsername, String id) {

    }

    @Override
    public <S extends Like> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Like> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer like) {
        return true;
    }

    @Override
    public Iterable<Like> findAll() {
        return null;
    }

    @Override
    public Iterable<Like> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

}
