package com.geoffwellington.manager.controller;

import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.service.UserService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userRequest) {
        UserDTO user = userService.createUser(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        UserDTO user = userService.getUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id,
                                              @RequestBody UserDTO userRequest) {
        UserDTO user = userService.updateUser(id, userRequest);
        HttpStatus status = user.getCreated().equals(user.getModified()) ? HttpStatus.CREATED : HttpStatus.OK;

        return new ResponseEntity<>(user, status);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
