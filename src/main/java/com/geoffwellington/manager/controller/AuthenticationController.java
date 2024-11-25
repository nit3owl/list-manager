package com.geoffwellington.manager.controller;

import com.geoffwellington.manager.model.dto.AuthResponse;
import com.geoffwellington.manager.model.dto.RegistrationRequest;
import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.service.AuthenticationService;
import com.geoffwellington.manager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    private final AuthenticationService authenticationService;


    public AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }
}
