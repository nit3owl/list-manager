package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;

import java.util.UUID;

public interface UserService {

    User createUser(User user);

    User getUser(UUID id);

    User updateUser(User user);

    void deleteUser(UUID id);

    Iterable<User> findBySubscription(SubscriptionType type);
}
