package com.geoffwellington.manager.service;

import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.model.dto.RegistrationRequest;
import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.repository.UserRepository;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceV1 implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceV1(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(RegistrationRequest registrationRequest) {
        if (!EmailValidator.getInstance().isValid(registrationRequest.getEmail())) {
            throw new ValidationException("Please provide a valid email address.",
                    new IllegalArgumentException(registrationRequest.getEmail()));
        }

        if (StringUtils.isEmpty(registrationRequest.getFirstName()) ||
                StringUtils.isEmpty(registrationRequest.getLastName())) {
            throw new ValidationException("Please provide a valid first name and last name.",
                    new IllegalArgumentException(
                            String.format("fName: %s, lName: %s", registrationRequest.getFirstName(),
                                    registrationRequest.getLastName())));
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new ValidationException("User already registered with that email.");
        }

        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setSubscriptionType(SubscriptionType.FREE); // set default subscription
        user.setMemberships(Collections.emptySet());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        User created = userRepository.save(user);

        return convert(created);
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

    @Override
    public UserDetails getUserDetails(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user exists with the specified ID."));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.emptySet());
    }

    private UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convert(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
