package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    AuthResponse login(HttpServletRequest request);
}
