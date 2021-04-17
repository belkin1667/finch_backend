package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("database_subs")
public interface SubsDAO extends CrudRepository<Subscription, Integer> {

    Iterable<Subscription> findSubscriptionsBySubscriber(String subscriber);

    Iterable<Subscription> findSubscriptionsByUsername(String username);

    long countByUsername(String username);

    long countBySubscriber(String username);

    boolean existsByUsername(String username);

    boolean existsBySubscriber(String username);

    boolean existsByUsernameAndSubscriber(String username, String subscriber);

    @Transactional
    void deleteByUsernameAndSubscriber(String myUsername, String subscription);
}
