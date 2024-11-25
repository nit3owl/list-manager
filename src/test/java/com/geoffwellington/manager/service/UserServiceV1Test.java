package com.geoffwellington.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.SubscriptionType;
import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.model.dto.RegistrationRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceV1Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceV1(userRepository, modelMapper, passwordEncoder);
    }

    @Test
    public void doesCreateNewUserWithFreeSubscription() {
        RegistrationRequest anyValidRegistrtationRequest = new RegistrationRequest();
        anyValidRegistrtationRequest.setFirstName("AnyFirstName");
        anyValidRegistrtationRequest.setLastName("AnyLastName");
        anyValidRegistrtationRequest.setEmail("anyValidEmail@foo.bar");

        User user = new User();
        user.setFirstName("AnyFirstName");
        user.setLastName("AnyLastName");
        user.setEmail("anyValidEmail@foo.bar");
        user.setSubscriptionType(SubscriptionType.FREE);

        UserDTO newUserDto = new UserDTO();
        newUserDto.setFirstName("AnyFirstName");
        newUserDto.setLastName("AnyLastName");
        newUserDto.setEmail("anyValidEmail@foo.bar");
        newUserDto.setSubscriptionType(SubscriptionType.FREE);

        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(newUserDto);

        UserDTO created = userService.registerUser(anyValidRegistrtationRequest);
        assertNotNull(created);
        assertEquals(SubscriptionType.FREE, created.getSubscriptionType());
    }

    @Test
    public void shouldRejectUserWithoutFirstName() {
        RegistrationRequest registrationRequestWithoutFName = new RegistrationRequest();
        registrationRequestWithoutFName.setFirstName(null);
        registrationRequestWithoutFName.setLastName("AnyLastName");
        registrationRequestWithoutFName.setEmail("anyValidEmail@foo.bar");

        assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequestWithoutFName),
                "Expected ValidationException");
    }

    @Test
    public void shouldRejectUserWithoutLastName() {
        RegistrationRequest registrationRequestWithoutLName = new RegistrationRequest();
        registrationRequestWithoutLName.setFirstName(null);
        registrationRequestWithoutLName.setLastName("AnyLastName");
        registrationRequestWithoutLName.setEmail("anyValidEmail@foo.bar");

        assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequestWithoutLName),
                "Expected ValidationException");
    }

    @Test
    public void shouldRejectUserWithInvalidEmail() {
        RegistrationRequest registrationRequestWithInvalidEmail = new RegistrationRequest();
        registrationRequestWithInvalidEmail.setFirstName(null);
        registrationRequestWithInvalidEmail.setLastName("AnyLastName");
        registrationRequestWithInvalidEmail.setEmail("abc");

        assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequestWithInvalidEmail),
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