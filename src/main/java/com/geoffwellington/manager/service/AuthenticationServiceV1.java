package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationServiceV1 implements AuthenticationService {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic ";

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthenticationServiceV1(JwtService jwtService, AuthenticationManager authenticationManager,
                                   UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public AuthResponse login(HttpServletRequest request) {
        // Extract Basic Authentication header
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BASIC)) {
            throw new BadCredentialsException("Missing or invalid Authorization header");
        }

        // Decode Basic Authentication credentials
        String base64Credentials = authHeader.substring(BASIC.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);

        if (values.length != 2) {
            throw new BadCredentialsException("Invalid Basic Authentication format");
        }

        // Authenticate
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(values[0], values[1])
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT
        UserDetails user = userService.getUserDetails(UUID.fromString(values[0]));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
