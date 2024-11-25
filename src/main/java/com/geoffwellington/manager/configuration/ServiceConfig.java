package com.geoffwellington.manager.configuration;

import com.geoffwellington.manager.repository.ManagedListRepository;
import com.geoffwellington.manager.repository.UserRepository;
import com.geoffwellington.manager.service.AuthenticationService;
import com.geoffwellington.manager.service.AuthenticationServiceV1;
import com.geoffwellington.manager.service.JwtService;
import com.geoffwellington.manager.service.ManagedListService;
import com.geoffwellington.manager.service.ManagedListServiceV1;
import com.geoffwellington.manager.service.UserService;
import com.geoffwellington.manager.service.UserServiceV1;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService(UserRepository userRepository, ModelMapper modelMapper,
                                   PasswordEncoder passwordEncoder) {
        return new UserServiceV1(userRepository, modelMapper, passwordEncoder);
    }

    @Bean
    public ManagedListService managedListService(ManagedListRepository managedListRepository, UserService userService,
                                                 ModelMapper modelMapper) {
        return new ManagedListServiceV1(managedListRepository, userService, modelMapper);
    }

    @Bean
    public AuthenticationService authenticationService(JwtService jwtService,
                                                       AuthenticationManager authenticationManager,
                                                       UserService userService) {
        return new AuthenticationServiceV1(jwtService, authenticationManager, userService);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
