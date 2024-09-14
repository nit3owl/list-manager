package com.geoffwellington.manager.service;

import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class UserServiceV1 implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;

    public UserServiceV1(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // set default subscription
        user.setSubscriptionType(SubscriptionType.FREE);
        user.setMemberships(Collections.emptySet());

        return userRepository.save(user);
    }

    @Override
    public User getUser(UUID id) {
        Optional<User> queryResult = userRepository.findById(id);
        if (queryResult.isEmpty()) {
            throw new UserNotFoundException("No user exists with the specified ID.");
        }

        return queryResult.get();
    }

    @Override
    public User updateUser(User user) {
        if (null == user.getId()) {
            throw new IllegalArgumentException("Cannot update a resource without an ID.");
        }

        Optional<User> existing = userRepository.findById(user.getId());

        // Only first/last name are updatable via the User API
        if (existing.isEmpty()) {
            throw new UserNotFoundException("No user exists with the specified ID.");
        }

        User existingUser = existing.get();
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(new User(id));
    }

    @Override
    public Iterable<User> findBySubscription(SubscriptionType type) {
        Iterable<User> users = userRepository.findBySubscriptionType(type);

        return users;
    }
}
