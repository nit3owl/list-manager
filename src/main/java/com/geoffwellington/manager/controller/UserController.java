package com.geoffwellington.manager.controller;

import com.geoffwellington.manager.model.User;
import com.geoffwellington.manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        User user = userService.createUser(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User userRequest) {
        User user = userService.updateUser(userRequest);
        HttpStatus status = user.getCreated().equals(user.getModified()) ? HttpStatus.CREATED : HttpStatus.OK;

        return new ResponseEntity<>(user, status);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
