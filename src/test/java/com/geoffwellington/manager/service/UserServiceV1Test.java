package com.geoffwellington.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceV1Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceV1(userRepository, modelMapper);
    }

    @Test
    public void doesCreateNewUserWithFreeSubscription() {
        UserDTO anyNewUser = new UserDTO();
        anyNewUser.setFirstName("AnyFirstName");
        anyNewUser.setLastName("AnyLastName");
        anyNewUser.setEmail("anyValidEmail@foo.bar");

        User user = new User();
        user.setFirstName("AnyFirstName");
        user.setLastName("AnyLastName");
        user.setEmail("anyValidEmail@foo.bar");
        user.setSubscriptionType(SubscriptionType.FREE);

        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(anyNewUser, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(anyNewUser);

        UserDTO created = userService.createUser(anyNewUser);
        assertNotNull(created);
        assertEquals(SubscriptionType.FREE, created.getSubscriptionType());
    }

    @Test
    public void shouldRejectUserWithoutFirstName() {
        UserDTO anyNewUser = new UserDTO();
        anyNewUser.setFirstName(null);
        anyNewUser.setLastName("AnyLastName");
        anyNewUser.setEmail("anyValidEmail@foo.bar");

        assertThrows(ValidationException.class, () -> userService.createUser(anyNewUser),
                "Expected ValidationException");
    }

    @Test
    public void shouldRejectUserWithoutLastName() {
        UserDTO anyNewUser = new UserDTO();
        anyNewUser.setFirstName("AnyFirstName");
        anyNewUser.setLastName(null);
        anyNewUser.setEmail("anyValidEmail@foo.bar");

        assertThrows(ValidationException.class, () -> userService.createUser(anyNewUser),
                "Expected ValidationException");
    }

    @Test
    public void shouldRejectUserWithInvalidEmail() {
        UserDTO anyNewUser = new UserDTO();
        anyNewUser.setFirstName("AnyFirstName");
        anyNewUser.setLastName("AnyLastName");
        anyNewUser.setEmail("abc");

        assertThrows(ValidationException.class, () -> userService.createUser(anyNewUser),
                "Expected ValidationException");
    }

    @Test
    public void shouldReturnExistingUserWhenIdValid() {
        UUID anyExistingUserId = UUID.randomUUID();

        UserDTO anyExistingUser = new UserDTO();
        anyExistingUser.setId(anyExistingUserId);

        User foundUser = new User();
        foundUser.setId(anyExistingUserId);

        when(userRepository.findById(anyExistingUserId)).thenReturn(Optional.of(foundUser));
        when(modelMapper.map(foundUser, UserDTO.class)).thenReturn(anyExistingUser);

        UserDTO userDTO = userService.getUser(anyExistingUserId);
        assertNotNull(userDTO);
    }

    @Test
    public void shouldThrowExceptionWhenUserIdNotFound() {
        UUID anyNonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(anyNonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(anyNonExistentUserId),
                "Expected UserNotFoundException");
    }

    @Test
    public void shouldDeleteExistingUser() {
        UUID anyExistingUserId = UUID.randomUUID();

        userService.deleteUser(anyExistingUserId);
    }

    @Test
    public void shouldThrowExceptionWhenIdNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, new UserDTO()),
                "Expected IllegalArgumentException");
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingAndIdAndUserIdDoNotMatch() {
        UUID anyNonExistentUserId = UUID.randomUUID();
        UserDTO nonExistentUser = new UserDTO();
        nonExistentUser.setId(UUID.randomUUID());
        nonExistentUser.setFirstName("AnyFirstName");
        nonExistentUser.setLastName(null);
        nonExistentUser.setEmail("anyValidEmail@foo.bar");

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(anyNonExistentUserId, nonExistentUser),
                "Expected IllegalArgumentException");
    }

    @Test
    public void shouldThrowExceptionWhenTryingToUpdateNonExistentUser() {
        UUID anyNonExistentUserId = UUID.randomUUID();
        UserDTO nonExistentUser = new UserDTO();
        nonExistentUser.setId(anyNonExistentUserId);
        nonExistentUser.setFirstName("AnyFirstName");
        nonExistentUser.setLastName(null);
        nonExistentUser.setEmail("anyValidEmail@foo.bar");

        when(userRepository.findById(anyNonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(anyNonExistentUserId, nonExistentUser),
                "Expected UserNotFoundException");
    }

    @Test
    public void shouldUpdateFirstAndLastNameOfExistingUser() {
        UUID anyExistingUserId = UUID.randomUUID();
        UserDTO anyExistingUser = new UserDTO();
        anyExistingUser.setId(anyExistingUserId);
        anyExistingUser.setFirstName("AnyFirstName");
        anyExistingUser.setLastName("AnyLastName");
        anyExistingUser.setEmail("anyValidEmail@foo.bar");

        User foundUser = new User();
        foundUser.setId(anyExistingUserId);
        foundUser.setFirstName("AnyOldFirstName");
        foundUser.setLastName("AnyOldLastName");
        foundUser.setEmail("anyValidEmail@foo.bar");

        Optional<User> foundUserOptional = Optional.of(foundUser);

        when(userRepository.findById(anyExistingUserId)).thenReturn(foundUserOptional);
        when(userRepository.save(foundUser)).thenReturn(foundUser);
        when(modelMapper.map(foundUser, UserDTO.class)).thenReturn(anyExistingUser);

        UserDTO updated = userService.updateUser(anyExistingUserId, anyExistingUser);

        assertNotNull(updated);
    }
}