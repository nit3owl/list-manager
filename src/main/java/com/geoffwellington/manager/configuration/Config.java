package com.geoffwellington.manager.configuration;

import com.geoffwellington.manager.repository.ManagedListRepository;
import com.geoffwellington.manager.repository.UserRepository;
import com.geoffwellington.manager.service.ManagedListService;
import com.geoffwellington.manager.service.ManagedListServiceV1;
import com.geoffwellington.manager.service.UserService;
import com.geoffwellington.manager.service.UserServiceV1;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public UserService userService(UserRepository userRepository, ModelMapper modelMapper) {
        return new UserServiceV1(userRepository, modelMapper);
    }

    @Bean
    public ManagedListService managedListService(ManagedListRepository managedListRepository, UserService userService,
                                                 ModelMapper modelMapper) {
        return new ManagedListServiceV1(managedListRepository, userService, modelMapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
