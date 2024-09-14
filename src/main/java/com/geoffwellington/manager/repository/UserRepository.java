package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User save(User user);

    Optional<User> findById(UUID id);

    void delete(User user);

    Iterable<User> findBySubscriptionType(SubscriptionType type);
}
