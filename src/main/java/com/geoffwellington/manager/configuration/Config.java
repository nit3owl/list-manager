package com.geoffwellington.manager.configuration;

import com.geoffwellington.manager.repository.UserRepository;
import com.geoffwellington.manager.service.ManagedListService;
import com.geoffwellington.manager.service.ManagedListServiceV1;
import com.geoffwellington.manager.service.UserService;
import com.geoffwellington.manager.service.UserServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ManagedListService managedListService() {
        return new ManagedListServiceV1();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceV1(userRepository);
    }
}
