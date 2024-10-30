package com.geoffwellington.manager.service;

import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.repository.UserRepository;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceV1 implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceV1(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("Please provide a valid email address.",
                    new IllegalArgumentException(user.getEmail()));
        }

        if (StringUtils.isEmpty(user.getFirstName()) || StringUtils.isEmpty(user.getLastName())) {
            throw new ValidationException("Please provide a valid first name and last name.",
                    new IllegalArgumentException(
                            String.format("fName: %s, lName: %s", user.getFirstName(), user.getLastName())));
        }

        // set default subscription
        user.setSubscriptionType(SubscriptionType.FREE);
        user.setMemberships(Collections.emptySet());

        return convert(userRepository.save(convert(user)));
    }

    @Override
    public UserDTO getUser(UUID id) {
        Optional<User> queryResult = userRepository.findById(id);
        if (queryResult.isEmpty()) {
            throw new UserNotFoundException("No user exists with the specified ID.");
        }

        return convert(queryResult.get());
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO user) {
        if (id == null || StringUtils.isEmpty(id.toString())) {
            throw new IllegalArgumentException("Cannot update a resource without an ID.");
        }

        if (id != user.getId()) {
            throw new IllegalArgumentException("Validate ID and User match.");
        }

        Optional<User> existing = userRepository.findById(id);

        if (existing.isEmpty()) {
            throw new UserNotFoundException("No user exists with the specified ID.");
        }

        // Only first/last name are updatable via the User API
        User existingUser = existing.get();
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        return convert(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<UserDTO> findBySubscription(SubscriptionType type) {
        Iterable<User> users = userRepository.findBySubscriptionType(type);

        List<UserDTO> userDTOList = new ArrayList<>();
        modelMapper.map(users, userDTOList);

        return userDTOList;
    }

    private UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convert(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
