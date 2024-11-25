package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.dto.RegistrationRequest;
import com.geoffwellington.manager.model.dto.UserDTO;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDTO registerUser(RegistrationRequest registrationRequest);

    UserDTO getUser(UUID id);

    UserDTO updateUser(UUID id, UserDTO user);

    void deleteUser(UUID id);

    UserDetails getUserDetails(UUID id);

    Iterable<UserDTO> findBySubscription(SubscriptionType type);
}
