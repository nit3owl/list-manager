package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.dto.UserDTO;
import java.util.UUID;

public interface UserService {

    UserDTO createUser(UserDTO user);

    UserDTO getUser(UUID id);

    UserDTO updateUser(UUID id, UserDTO user);

    void deleteUser(UUID id);

    Iterable<UserDTO> findBySubscription(SubscriptionType type);
}
