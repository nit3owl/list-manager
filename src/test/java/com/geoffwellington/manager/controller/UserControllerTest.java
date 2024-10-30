package com.geoffwellington.manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController(userService);
    }

    @Test
    public void shouldReturnNewUserAnd201WhenBodyValid() {
        UserDTO anyValidDTO = new UserDTO();
        when(userService.createUser(anyValidDTO)).thenReturn(new UserDTO());

        ResponseEntity<UserDTO> response = userController.createUser(anyValidDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldUpdateExistingUserAndReturn200WhenIdAndBodyValid() {
        UserDTO anyExistingUserDTO = new UserDTO();
        anyExistingUserDTO.setModified(Instant.now());
        anyExistingUserDTO.setCreated(Instant.now().minus(10, ChronoUnit.SECONDS));

        UUID anyMatchingID = UUID.randomUUID();
        when(userService.updateUser(anyMatchingID, anyExistingUserDTO)).thenReturn(anyExistingUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(anyMatchingID, anyExistingUserDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldCreateUserAndReturn200WhenIdWasNotValidButBodyIs() {
        UserDTO anyExistingUserDTO = new UserDTO();
        Instant time = Instant.now();
        anyExistingUserDTO.setModified(time);
        anyExistingUserDTO.setCreated(time);

        UUID anyMatchingID = UUID.randomUUID();
        when(userService.updateUser(anyMatchingID, anyExistingUserDTO)).thenReturn(anyExistingUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(anyMatchingID, anyExistingUserDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldDeleteAndReturn200WhenIdValid() {
        UUID anyMatchingID = UUID.randomUUID();

        ResponseEntity<UserDTO> response = userController.deleteUser(anyMatchingID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturnUserAnd200WhenIdValid() {
        UserDTO anyValidDTO = new UserDTO();
        UUID anyMatchingID = UUID.randomUUID();
        when(userService.getUser(anyMatchingID)).thenReturn(anyValidDTO);

        ResponseEntity<UserDTO> response = userController.getUser(anyMatchingID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}