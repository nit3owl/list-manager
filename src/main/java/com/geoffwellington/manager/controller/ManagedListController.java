package com.geoffwellington.manager.controller;

import com.geoffwellington.manager.model.ManagedList;
import com.geoffwellington.manager.service.ManagedListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

@RestController
@RequestMapping("lists")
public class ManagedListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ManagedListService managedListService;

    public ManagedListController(ManagedListService managedListService) {
        this.managedListService = managedListService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ManagedList> getManagedList(@PathVariable UUID id) {
        ManagedList managedList = managedListService.getManagedList(id);
        return new ResponseEntity<>(managedList, HttpStatus.OK);
    }
}
