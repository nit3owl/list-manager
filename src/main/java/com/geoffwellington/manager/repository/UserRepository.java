package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User save(User user);

    Optional<User> findById(UUID id);

    void deleteById(UUID id);

    Iterable<User> findBySubscriptionType(SubscriptionType type);
}
